package com.example.demo.entities;

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.*;
//import java.util.Collection;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    @Email
    private String  email;

    private String firstName, profPic, lastName, passwordHash, physicalAddress, country, dob, city;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<UserRole> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<DogModel> dogs = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<MediaFile> files = new HashSet<>();
}
