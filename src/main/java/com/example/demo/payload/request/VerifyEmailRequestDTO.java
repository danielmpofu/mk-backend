package com.example.demo.payload.request;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailRequestDTO {
    private String emailAddress;
}