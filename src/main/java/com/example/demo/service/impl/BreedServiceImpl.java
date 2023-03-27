package com.example.demo.service.impl;

import com.example.demo.entities.BreedModel;
import com.example.demo.payload.request.CreateDogDTO;
import com.example.demo.repo.BreedModelRepository;
import com.example.demo.service.BreedService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BreedServiceImpl implements BreedService {

   private final BreedModelRepository breedModelRepository;
   private ModelMapper modelMapper;

    public BreedServiceImpl(BreedModelRepository breedModelRepository) {
        this.breedModelRepository = breedModelRepository;
        modelMapper = new ModelMapper();
    }

    @Override
    public BreedModel createBreed(CreateDogDTO createDogDTO) {
        BreedModel breedModel = modelMapper.map(createDogDTO, BreedModel.class);
        return breedModelRepository.save(breedModel);
    }

    @Override
    public List<BreedModel> getBreeds() {
        return breedModelRepository.findAll();
    }

    @Override
    public Optional<BreedModel> getBreed(long id) {
        return breedModelRepository.findById(id);
    }

    @Override
    public BreedModel editBreed(long breedId, BreedModel breedModel) {
        return breedModelRepository.save(breedModel);
    }
}
