package com.example.demo.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Data
public class CreateDogDTO {
    String name,
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

    boolean isAlive;
//    MultipartFile primaryPic;
}
