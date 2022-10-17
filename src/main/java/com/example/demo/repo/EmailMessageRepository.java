package com.example.demo.repo;

import com.example.demo.entities.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
}