package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DogModel extends Auditable {

    @Id
    @GeneratedValue
    @Setter @Getter
    private Long id;

    @Setter @Getter
    private String name, birthPlace, notes, regNumber, breeder, breed, sponsor, sponsorInfo, color, gender,primaryPic,deathNotes;

    @Setter @Getter
    private Date dob, regDate, dod;

    @Setter @Getter
    private boolean isAlive;

    @Setter @Getter
    private boolean deleted;

    @Setter @Getter
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    private List<MediaFile> files = new ArrayList<>();
}

