package com.example.demo.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
@Data
public class UserRegistrationDTO {

        //todo change dob datatype to date and do the jackson thing as mentioned by kumbi
        //https://www.baeldung.com/jackson-serialize-dates
        private String city;
        private String country;
        private String dob,profPic;


        @NotBlank(message = "Email address is required")
        @Email(message = "not a valid email address")
        private String email;
        private String firstName;
        private String lastName;
        private String passwordHash;
        private String phoneNumber;
        private String physicalAddress;
        private ArrayList<RoleCreationDTO> roles;
        private String userName;

        @Override
        public String toString() {
                return "UserRegistrationDTO{" +
                        "city='" + city + '\'' +
                        ", country='" + country + '\'' +
                        ", dob='" + dob + '\'' +
                        ", profPic='" + profPic + '\'' +
                        ", email='" + email + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", passwordHash='" + passwordHash + '\'' +
                        ", phoneNumber='" + phoneNumber + '\'' +
                        ", physicalAddress='" + physicalAddress + '\'' +
                        ", roles=" + roles +
                        ", userName='" + userName + '\'' +
                        '}';
        }
}
