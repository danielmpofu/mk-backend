package com.example.demo.controller;

import com.example.demo.payload.request.CreateDogDTO;
import com.example.demo.payload.request.DogUpdateDTO;
import com.example.demo.payload.response.DogResponseDTO;
import com.example.demo.entities.DogModel;
import com.example.demo.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dogs/")
@Slf4j
public class DogController {

    final DogService dogService;
    private final ModelMapper objectMapper;

    @GetMapping()
    @Operation(summary = "Get a list of all dogs in the system")
    public ResponseEntity<List<DogResponseDTO>> getAllDogs() {
        return ResponseEntity.ok().body(dogService
                .findAll()
                .stream()
                .parallel()
                .map(dogModel -> {
                    return objectMapper.map(dogModel, DogResponseDTO.class);
                }).toList());
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    @Operation(summary = "Post a dog creation dto and save the dog if it does not exist")
    public ResponseEntity<DogResponseDTO> saveNewDog(@ModelAttribute CreateDogDTO createDogDTO, @RequestPart("primaryPic") MultipartFile primaryPic) {

        File directory = new File("public/uploads/");
        if (!directory.exists()) {
            try {
                if (directory.mkdirs()) {
                    log.info("upload directory just got created now");
                }
            } catch (SecurityException se) {
                log.error(se.toString());
            }
        }

        String fileName = (System.currentTimeMillis() + "_" + primaryPic.getOriginalFilename()).replace(" ", "_");

        final String pathToDir = directory.getPath() + File.separator + fileName;

        try {
            File path = new File(pathToDir);
            boolean hasSavedFile = path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(primaryPic.getBytes());
            output.close();

            DogModel dogToSave = objectMapper.map(createDogDTO, DogModel.class);
            dogToSave.setPrimaryPic(pathToDir);
            dogToSave.setRegDate(new Date());

            return ResponseEntity.status(201).body(objectMapper.map(dogService.saveDogModel(dogToSave, path)
                    , DogResponseDTO.class));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogResponseDTO> getSpecificDog(@PathVariable Long id) {
        DogModel dogModel = dogService.findDogModelByIdAndDeleted(id);
        return ResponseEntity.ok().body(objectMapper.map(dogModel, DogResponseDTO.class));
    }

    @PutMapping()
    public ResponseEntity<DogResponseDTO> updateDog(@RequestBody DogUpdateDTO dogUpdateDTO) {
        DogModel updatedDog = dogService.updateDogModel(dogUpdateDTO.getId(), objectMapper.map(dogUpdateDTO, DogModel.class));
        return ResponseEntity.ok().body(objectMapper.map(updatedDog, DogResponseDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

}
