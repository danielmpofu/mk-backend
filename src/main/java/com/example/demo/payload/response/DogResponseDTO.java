package com.example.demo.payload.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;


@Data

public class DogResponseDTO {

    private long id;
    String name, birthPlace, notes, regNumber, breeder, breed, sponsor, sponsorInfo, color, gender, primaryPic, deathNotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date dob, regDate, dod;
    boolean isAlive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date dateCreated;
    private Long modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date dateModified;
    private Long createdBy;
}
