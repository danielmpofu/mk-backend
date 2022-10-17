package com.example.demo.util;


import com.example.demo.entities.EmailMessage;
import com.example.demo.entities.MediaFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class RandomMethods {
    public String detectMimetype(File file){
        Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Resource> serveFile(MediaFile mediaFile){
        try {
            Path path = Paths.get(mediaFile.getUploadPath()).normalize();
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaFile.getUploadPath() + "\"")
                    .contentType(MediaType.parseMediaType(new RandomMethods().detectMimetype(new File( mediaFile.getUploadPath()))))
                    .body(resource);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

//    public boolean sendEmail(EmailMessage details) {
//
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom("danielmpofu123@gmail.com");
//            mailMessage.setTo(details.getEmailAddress());
//            mailMessage.setText(details.getEmailMessage());
//            mailMessage.setSubject(details.getEmailSubject());
//            javaMailSender.send(mailMessage);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }


}
