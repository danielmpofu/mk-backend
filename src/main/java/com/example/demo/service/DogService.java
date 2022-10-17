package com.example.demo.service;

import com.example.demo.entities.DogModel;
import com.example.demo.entities.MediaFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface DogService {

    public DogModel saveDogModel(DogModel dogModel, File file);

    public DogModel findDogModelByIdAndDeleted(long id);

    public List<DogModel> findAll();

    public DogModel attachMediaFile(Long dogId,MediaFile mediaFile);

    public DogModel updateDogModel(long id, DogModel dogModel);

    public void deleteById(long id);
}
