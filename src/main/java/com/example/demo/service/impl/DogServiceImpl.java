package com.example.demo.service.impl;

import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.Auditable;
import com.example.demo.entities.DogModel;
import com.example.demo.entities.MediaFile;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.repo.DogRepo;
import com.example.demo.service.AuthService;
import com.example.demo.service.DogService;
import com.example.demo.service.MediaFileService;
import com.example.demo.util.RandomMethods;
import com.sun.mail.imap.protocol.Item;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
//import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DogServiceImpl implements DogService {

    private final DogRepo dogRepo;

    private final AuthService authService;

    @Override
    public DogModel saveDogModel(DogModel dogModel, File file) {

        dogModel.setRegNumber("HNM" + new Random().nextInt(999, 99999));
        dogModel.setDeleted(false);

        dogModel.setDateCreated(LocalDateTime.now());
        dogModel.setDateModified(LocalDateTime.now());

        dogModel = dogRepo.save(dogModel);

        MediaFile mediaFile = new RandomMethods().createMediaFile(file,
                dogModel.getId(), String.format("%s Picture", dogModel.getRegNumber()));
        dogModel.getFiles().add(mediaFile);

        return dogModel;
    }

    @Override
    public DogModel findDogModelByIdAndDeleted(long id) {
        DogModel dog = dogRepo.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new ItemNotFoundException(id, DogModel.class.getName() + " here is the error"));

        return dog;
    }

    @Override
    public List<DogModel> findAll() {
        return dogRepo.findAllByDeleted(false);
    }


    @Override
    public DogModel attachMediaFile(Long dogId, MediaFile mediaFile) {
        DogModel thisDog = findDogModelByIdAndDeleted(dogId);
        thisDog.getFiles().add(mediaFile);
        return thisDog;
    }

    @Override
    public DogModel updateDogModel(long id, DogModel dogModel) {
        DogModel dogModel1 = findDogModelByIdAndDeleted(id);
        if (dogModel1 != null) {

        }
        return dogRepo.save(dogModel);
    }

    public DogModel findById(long dogId) {
        return dogRepo.findById(dogId).orElseThrow();
    }

    @Override
    public void deleteById(long id) {
        dogRepo.delete(findById(id));
        //soft delete the thing
//        DogModel dogModel = findDogModelByIdAndDeleted(id);
//       if(dogModel==null){
//           throw new ItemNotFoundException(id,DogModel.class.toString());
//       }
//        dogModel.setDeleted(true);
//        updateDogModel(id, dogModel);
//        dogRepo.deleteById(id);
    }
}
