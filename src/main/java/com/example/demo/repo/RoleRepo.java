package com.example.demo.repo;

import com.example.demo.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<UserRole, Long> {

    public UserRole findByName(String name);

    public UserRole findById(long id);
}
