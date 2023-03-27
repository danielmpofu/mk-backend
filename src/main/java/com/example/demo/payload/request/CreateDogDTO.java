package com.example.demo.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CreateDogDTO {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Setter
    @Getter
    private String name,
            birthPlace,
            notes,
            breeder,
            breed,
            sponsor,
            sponsorInfo,
            color,
            gender,
            deathNotes;
    Date dob, dod;

//    boolean isAlive = true;
    MultipartFile primaryPic;
}
