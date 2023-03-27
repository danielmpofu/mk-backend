package com.example.demo.payload.response;
import com.example.demo.entities.MediaFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data

public class DogResponseDTO {
    private long id;
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


}

