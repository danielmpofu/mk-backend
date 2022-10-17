package com.example.demo.payload.internal;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

@Setter @Getter
public class EmailVerificationDTO {

    public EmailVerificationDTO() {
        this.dateEnquired = new Date();
        this.expiryDate = DateUtils.addMinutes(this.dateEnquired,20);
    }

    private String ownerId, emailAddress,emailSubject,emailMessage, specialToken;
    private Date dateEnquired, expiryDate;

}
