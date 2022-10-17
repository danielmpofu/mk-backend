package com.example.demo.service.impl;


import com.example.demo.entities.EmailMessage;
import com.example.demo.payload.internal.EmailVerificationDTO;
import com.example.demo.payload.request.VerifyEmailRequestDTO;
import com.example.demo.repo.EmailMessageRepository;
import com.example.demo.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private  JavaMailSender javaMailSender;
    ObjectMapper objectMapper = new ObjectMapper();
    private EmailMessageRepository emailMessageRepository;

    @Value("spring.mail.username")
    private static String sender;

    @Override
    public EmailMessage verifyEmailAddress(VerifyEmailRequestDTO details) {
        EmailVerificationDTO emailVerificationDTO = objectMapper.convertValue(details, EmailVerificationDTO.class);
        emailVerificationDTO.setDateEnquired(new Date());
        emailVerificationDTO.setExpiryDate(DateUtils.addMinutes(emailVerificationDTO.getDateEnquired(), 20));
        emailVerificationDTO.setEmailSubject("Email Verification");
        emailVerificationDTO.setSpecialToken(RandomStringUtils.random(6, true, true));

        String emailMessageSB = "Hie there\n" +
                "You have requested for an email address verification" +
                " for My Kennelz\n\n Here is your verification token \n" +
                emailVerificationDTO.getSpecialToken() +
                "\nit expires at " +
                emailVerificationDTO.getExpiryDate() +
                "\nRegards\nMK Team";
        emailVerificationDTO.setEmailMessage(emailMessageSB);

        try {
            if (sendEmail(emailVerificationDTO)) {
                EmailMessage emailMessage = objectMapper.convertValue(emailVerificationDTO, EmailMessage.class);
                return emailMessageRepository.save(emailMessage);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean sendEmail(EmailVerificationDTO details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getEmailAddress());
            mailMessage.setText(details.getEmailMessage());
            mailMessage.setSubject(details.getEmailSubject());
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String sendMailWithAttachment(VerifyEmailRequestDTO details) {
        return null;
    }

    @Override
    public EmailMessage saveEmail(VerifyEmailRequestDTO verifyEmailRequestDTO) {
//        return emailMessageRepository.save()
        return null;
    }
}
