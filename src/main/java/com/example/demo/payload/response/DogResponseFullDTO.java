package com.example.demo.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DogResponseFullDTO {
    private long id, ownerId;
    String name, birthPlace, notes, regNumber, breeder, breed, sponsor, sponsorInfo, color, gender, primaryPic, deathNotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date dob, regDate, dod;
    boolean isAlive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime dateCreated;
    private Long modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime dateModified;
    private Long createdBy;

    private AttachmentUserProfileFull owner;
}
