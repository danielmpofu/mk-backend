package com.example.demo.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class EmailValidationResponseDTO {
    private String ownerId, emailAddress, emailSubject, emailMessage, specialToken;
    private Date dateEnquired, expiryDate;

}