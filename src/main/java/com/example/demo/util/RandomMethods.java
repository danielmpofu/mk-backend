package com.example.demo.util;


import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.MediaFile;
import com.example.demo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class RandomMethods {

    //    private final;
    public String detectMimetype(File file) {
        Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Resource> serveFile(MediaFile mediaFile) {
        try {
            Path path = Paths.get(mediaFile.getUploadPath()).normalize();
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaFile.getUploadPath() + "\"")
                    .contentType(MediaType.parseMediaType(new RandomMethods().detectMimetype(new File(mediaFile.getUploadPath()))))
                    .body(resource);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    public ApplicationUser getLoggedUser(AuthService authService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authService.getUserByUserName(authentication.getPrincipal().toString());
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


    public File createUploadDir() {
        File directory = new File("public/uploads/");
        if (!directory.exists()) {
            try {
                if (directory.mkdirs()) {
                    log.info("upload directory just got created now");
                }
            } catch (SecurityException se) {
                log.error(se.toString());
            }
        }
        return directory;
    }

    public String createFilePath(String fileName) {

        return new RandomMethods().createUploadDir().getPath()
                + File.separator + (System.currentTimeMillis() + "_"
                + fileName).replace(" ", "_");

    }

    public MediaFile createMediaFile(File file, long ownerId, String fileName) {
        MediaFile mediaFile = new MediaFile();
        String[] fileExt = file.getName().split("[.]");

        mediaFile.setFileName(fileName);
        mediaFile.setFileOwner(ownerId);
        try {
            mediaFile.setFileSize(Files.size(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaFile.setFileExtension(fileExt[fileExt.length - 1]);
        mediaFile.setFilePurpose("Picture");
        mediaFile.setUploadPath(file.getPath());
        mediaFile.setDateUploaded(System.currentTimeMillis());
        return mediaFile;
    }

}
