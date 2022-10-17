package com.example.demo.service.impl;

import com.example.demo.entities.DogModel;
import com.example.demo.entities.MediaFile;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.repo.DogRepo;
import com.example.demo.service.DogService;
import com.example.demo.service.MediaFileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Random;
//import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DogServiceImpl implements DogService {

    private final DogRepo dogRepo;

    @Override
    public DogModel saveDogModel(DogModel dogModel, File file) {
        dogModel.setRegNumber("HNM" + new Random().nextInt(999, 99999));
        dogModel.setDeleted(false);

        dogModel = dogRepo.save(dogModel);

        MediaFile mediaFile = new MediaFile();
        String[] fileExt = file.getName().split("[.]");

        mediaFile.setFileName(String.format("%s Picture", dogModel.getRegNumber()));
        mediaFile.setFileOwner(dogModel.getId());
        try {
            mediaFile.setFileSize(Files.size(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaFile.setFileExtension(fileExt[fileExt.length - 1]);
        mediaFile.setFilePurpose("Picture");
        mediaFile.setUploadPath(dogModel.getPrimaryPic());
        mediaFile.setDateUploaded(System.currentTimeMillis());
        //save the media file here.
//        mediaFileService.saveMediaFile(mediaFile);

        dogModel.getFiles().add(mediaFile);
        return dogModel;
    }

    @Override
    public DogModel findDogModelByIdAndDeleted(long id) {
        return dogRepo.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new ItemNotFoundException(id, DogModel.class.getName()));
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

    @Override
    public void deleteById(long id) {
        //soft delete the thing
        DogModel dogModel = findDogModelByIdAndDeleted(id);
        dogModel.setDeleted(true);
        updateDogModel(id, dogModel);
//        dogRepo.deleteById(id);
    }
}
