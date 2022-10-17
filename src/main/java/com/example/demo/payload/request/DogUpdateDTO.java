package com.example.demo.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class DogUpdateDTO {
    private long id;
    String name, birthPlace, notes, regNumber, breeder, breed, sponsor, sponsorInfo, color, gender, primaryPic, deathNotes;
    Date dob, regDate, dod;
    boolean isAlive;
}
