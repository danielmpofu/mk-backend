package com.example.demo.entities;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class BreedModel {

    @Id
    @GeneratedValue
    private long id;
    private String breedName, picture, description;
}
