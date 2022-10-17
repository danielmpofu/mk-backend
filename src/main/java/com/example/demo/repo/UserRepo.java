package com.example.demo.repo;

import com.example.demo.entities.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<ApplicationUser,Long> {
    public ApplicationUser findApplicationUserByUserName(String userName);
    public ApplicationUser findApplicationUserById(long id);
}
