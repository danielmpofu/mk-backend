package com.example.demo.payload.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.example.demo.entities.AdvertModel} entity
 */
@Data
public class AdvertModelDTO implements Serializable {
    private final Date dateCreated;
    private final Date dateModified;
    private final Long createdBy;
    private final Long modifiedBy;
    private final long postedBy;
    private final String postTitle;
    private final String description;
    private final String category;
    private final String location;
    private final String phoneNumber;
    private final String phoneNumber2;
    private final String emailAddress;
    private final String condition;
    private final double price;
    private final double discountPercent;
    private final boolean inStock;
    private final boolean negotiable;
}