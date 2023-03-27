package com.example.demo.service;

import com.example.demo.entities.BreedModel;
import com.example.demo.payload.request.CreateDogDTO;

import java.util.List;
import java.util.Optional;

public interface BreedService {
    public BreedModel createBreed(CreateDogDTO createDogDTO);
    public List<BreedModel> getBreeds();
    public Optional<BreedModel> getBreed(long id);
    public BreedModel editBreed(long breedId, BreedModel breedModel);
}
