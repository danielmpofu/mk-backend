package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter @Setter
@Data
public class AdvertModel extends Auditable {
    @Id
    @GeneratedValue
    private long id;

    private long postedBy;

    private String postTitle, category, location, phoneNumber, phoneNumber2, emailAddress, condition;
    private  String primaryPic;

    @Column(length = 10000)
    private String description;
    //@Setter @Getter
    private double price, discountPercent;

    //@Setter @Getter
    private boolean inStock, negotiable;

    //@Setter @Getter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<MediaFile> attachments = new ArrayList<>();
}
