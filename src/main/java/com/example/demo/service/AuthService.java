package com.example.demo.service;

import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.UserRole;

import java.util.List;

public interface AuthService {
    ApplicationUser saveUser(ApplicationUser applicationUser);
    UserRole saveRole(UserRole userRole);
    void assignRoleToUser(String userName, String roleName);
    ApplicationUser getUserByUserName(String userName);
    ApplicationUser getUser(long userId);
    List<ApplicationUser> getUsers();
    List<UserRole> getUserRoles();

//    void register();
}
