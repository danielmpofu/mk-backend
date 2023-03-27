package com.example.demo.controller;


import com.example.demo.entities.BreedModel;
import com.example.demo.service.BreedService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("breeds")
public class BreedsController {

    private final BreedService breedService;
    private ModelMapper modelMapper;

    public BreedsController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping("/")
    public ResponseEntity<List<BreedModel>> listAll(){
        return ResponseEntity.status(200).body(breedService.getBreeds());
    }
}
