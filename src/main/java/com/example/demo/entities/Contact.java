package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Setter
    @Getter
    @Id
    @GeneratedValue
    private int id;

    @Setter @Getter
    private String name, email, address;

}
