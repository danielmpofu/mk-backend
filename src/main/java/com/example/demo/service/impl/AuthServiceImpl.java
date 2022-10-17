package com.example.demo.service.impl;

import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.UserRole;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepo.findApplicationUserByUserName(username);
        if (user == null) {
            log.error("User {} was not found in the database", username);
            throw new UsernameNotFoundException("User was not found in the database");
        } else {

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(userRole -> {
                authorities.add(new SimpleGrantedAuthority(userRole.getName()));
            });
            return new User(user.getUserName(), user.getPasswordHash(), authorities);

        }

    }


    @Override
    public ApplicationUser saveUser(ApplicationUser applicationUser) {
        applicationUser.setPasswordHash(passwordEncoder.encode(applicationUser.getPasswordHash()));
        log.info("Saving user {} to the database", applicationUser.getUserName());
        return userRepo.save(applicationUser);
    }

    @Override
    public UserRole saveRole(UserRole userRole) {
        log.info("saving user {} ", userRole.getName());
        return roleRepo.save(userRole);
    }

    @Override
    public void assignRoleToUser(String userName, String roleName) {
        ApplicationUser applicationUser = userRepo.findApplicationUserByUserName(userName);
        UserRole userRole = roleRepo.findByName(roleName);
        log.info("assigning role : {} to this user {} ", roleName, userName);
        applicationUser.getRoles().add(userRole);
    }

    @Override
    public ApplicationUser getUserByUserName(String userName) {
        return userRepo.findApplicationUserByUserName(userName);
    }

    @Override
    public ApplicationUser getUser(long userId) {
        log.info("fetching user {} ", userId);
        return userRepo.findApplicationUserById(userId);
    }

    @Override
    public List<ApplicationUser> getUsers() {
        log.info("Getting all users");
        return userRepo.findAll();
    }

    @Override
    public List<UserRole> getUserRoles() {
        return roleRepo.findAll();
    }


}
