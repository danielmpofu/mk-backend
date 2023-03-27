package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.exceptions.UnauthorizedAccess;
import com.example.demo.payload.request.AssignRoleDTO;
import com.example.demo.payload.request.UserRegistrationDTO;
import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.UserRole;
import com.example.demo.service.AuthService;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {
    //login
    //reg
    //reset password
    //sso

    private final AuthService authService;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    private final ModelMapper objectMapper;


    @GetMapping("/users/all")
    public ResponseEntity<List<ApplicationUser>> getUsers() {
        return ResponseEntity.ok().body(authService.getUsers());
    }

    @PostMapping("/users/register")
    public ResponseEntity<ApplicationUser> registerUser(@RequestBody UserRegistrationDTO userData) {
        ApplicationUser user = objectMapper.map(userData, ApplicationUser.class);
        user.setProfPic("");
        return ResponseEntity.ok().body(authService.saveUser(user));
    }

    @GetMapping("/users/one/{id}")
    public ResponseEntity<ApplicationUser> getById(@PathVariable long id) {
        return ResponseEntity.ok().body(authService.getUser(id));
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<ApplicationUser> getByUserName(@PathVariable String username) {
        return ResponseEntity.ok().body(authService.getUserByUserName(username));
    }

    @PostMapping("/users/assign/")
    public ResponseEntity<?> assignRole(@RequestBody AssignRoleDTO assignRoleDTO) {
        authService.assignRoleToUser(assignRoleDTO.getUsername(), assignRoleDTO.getRoleName());
        return ResponseEntity.ok().body(assignRoleDTO);
    }

    @PostMapping("/roles/")
    public ResponseEntity<UserRole> saveRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok().body(authService.saveRole(userRole));
    }

    @GetMapping("/refresh/")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            try {
                String refreshToken = authorization.split(" ")[1];
                Algorithm algorithm = Algorithm.HMAC256("my secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                ApplicationUser loggedInUser = authService.getUserByUserName(userName);

                String accessToken = JWT.create()
                        .withSubject(loggedInUser.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 360 * 24)))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", loggedInUser.getRoles().stream().map(UserRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);


                HashMap<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                tokens.put("userName", loggedInUser.getUserName());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

                return ResponseEntity.ok().body(tokens);

            } catch (Exception e) {
                log.error(e.getMessage());
//log.error(e.getStackTrace().toString());
                throw new UnauthorizedAccess("You are not allowed to access this resource " + e.getMessage());
            }
        } else {
            throw new RuntimeException("You are not allowed to access this resource, invalid token");
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<UserRole>> getRoles() {
        return ResponseEntity.ok().body(authService.getUserRoles());
    }


}
