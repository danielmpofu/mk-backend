package com.example.demo.payload.response;

import com.example.demo.entities.DogModel;
import com.example.demo.entities.MediaFile;
import com.example.demo.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.*;

@Data
public class UserResponseDTO {
    private String firstName,profPic,
            userName,lastName, email, passwordHash, phoneNumber, physicalAddress, country,city;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date  dob;

    private Collection<UserRole> roles;

    private Set<DogModel> dogs;

    private Set<MediaFile> files;
}
