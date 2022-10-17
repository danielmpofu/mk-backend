package com.example.demo.controller;

import com.example.demo.payload.request.AssignRoleDTO;
import com.example.demo.payload.request.UserRegistrationDTO;
import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.UserRole;
import com.example.demo.service.AuthService;
//import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok().build();
    }

    @PostMapping("/roles/")
    public ResponseEntity<UserRole> saveRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok().body(authService.saveRole(userRole));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<UserRole>> getRoles() {
        return ResponseEntity.ok().body(authService.getUserRoles());
    }



}
