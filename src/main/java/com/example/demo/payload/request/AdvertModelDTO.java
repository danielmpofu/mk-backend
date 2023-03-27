package com.example.demo.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.example.demo.entities.AdvertModel} entity
 */
@Data
public class AdvertModelDTO implements Serializable {
//    private Date dateCreated;
//    private Date dateModified;
//    private Long createdBy;
//    private Long modifiedBy;
//    private long postedBy;
    private String postTitle;
    private String description;
    private String category;
    private String location;
    private String phoneNumber;
    private String phoneNumber2;
    private String emailAddress;
    private String condition;
    private MultipartFile primaryPic;
    private double price;
    private double discountPercent;
    private boolean inStock;
    private boolean negotiable;
}