package com.example.demo.service;

import com.example.demo.entities.EmailMessage;
import com.example.demo.payload.internal.EmailVerificationDTO;
import com.example.demo.payload.request.VerifyEmailRequestDTO;

public interface EmailService {
    EmailMessage verifyEmailAddress(VerifyEmailRequestDTO details);
    boolean sendEmail(EmailVerificationDTO mailMessage);
    String sendMailWithAttachment(VerifyEmailRequestDTO details);
    EmailMessage saveEmail(VerifyEmailRequestDTO verifyEmailRequestDTO);
}
