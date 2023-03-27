package com.example.demo.controller;

import com.example.demo.entities.ApplicationUser;
import com.example.demo.entities.MediaFile;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.payload.request.CreateDogDTO;
import com.example.demo.payload.request.DogUpdateDTO;
import com.example.demo.payload.request.FileAttachmentDTO;
import com.example.demo.payload.response.AttachmentUserProfileFull;
import com.example.demo.payload.response.DogResponseDTO;
import com.example.demo.entities.DogModel;
import com.example.demo.payload.response.DogResponseFullDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.DogService;
import com.example.demo.util.RandomMethods;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dogs/")
@Slf4j
public class DogController {

    private final DogService dogService;
    private final ModelMapper objectMapper;
    private final AuthService authService;

    @GetMapping()
    @Operation(summary = "Get a list of all dogs in the system")
    public ResponseEntity<List<DogResponseDTO>> getAllDogs() {
        return ResponseEntity.ok().body(dogService
                .findAll()
                .stream()
                .parallel()
                .map((DogModel dogModel) -> {
                    DogResponseDTO dogResponseDTO = objectMapper.map(dogModel, DogResponseDTO.class);
                    dogResponseDTO.setPrimaryPic(String.format("baseurl-here/api/v1/media/fileid/%s", dogModel.getFiles().get(0).getId()));
                    return dogResponseDTO;
                }).toList());
    }

    @GetMapping("owner/{username}")
    @Operation(summary = "Get a list of all dogs in the system uploaded by specific user")
    public ResponseEntity<List<DogResponseDTO>> getAllDogsByOwner(@PathVariable String username) {
        ApplicationUser applicationUser = authService.getUserByUserName(username);
        if (applicationUser != null) {

            return ResponseEntity.ok().body(
                    applicationUser
                            .getDogs()
                            .stream()
                            .parallel()
                            .map((DogModel dogModel) -> {
                                DogResponseDTO dogResponseDTO = objectMapper.map(dogModel, DogResponseDTO.class);
                                dogResponseDTO.setPrimaryPic(String.format("baseurl-here/api/v1/media/fileid/%s", dogModel.getFiles().get(0).getId()));
                                return dogResponseDTO;
                            }).toList());
        } else {
            throw new ItemNotFoundException(0L, username);
        }
    }

    @PostMapping(path = "attach", consumes = {"multipart/form-data", "application/json"})
    @Operation(summary = "post a file and id of the dog that you want to add files to!")
    public ResponseEntity<DogResponseFullDTO> attachDogFile(@ModelAttribute FileAttachmentDTO fileAttachmentDTO) {

        try {
            String pathToDir = new RandomMethods().createFilePath(fileAttachmentDTO.getFile().getOriginalFilename());
            File path = new File(pathToDir);

            boolean hasSavedFile = path.createNewFile();
            if (hasSavedFile) {
                FileOutputStream output = new FileOutputStream(path);
                output.write(fileAttachmentDTO.getFile().getBytes());
                output.close();

                DogModel dogModel = dogService.findDogModelByIdAndDeleted(fileAttachmentDTO.getOwnerId());
                if (dogModel == null) {
                    throw new ItemNotFoundException(fileAttachmentDTO.getOwnerId(), DogModel.class.getCanonicalName());
                }
                MediaFile mediaFile = new RandomMethods()
                        .createMediaFile(path, dogModel.getId(), String.format("%s Picture", dogModel.getRegNumber()));
                dogModel.getFiles().add(mediaFile);

                return getSpecificDog(fileAttachmentDTO.getOwnerId());
            } else {
                throw new RuntimeException("Unable to create files at the moment");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    @Operation(summary = "Post a dog creation dto and save the dog if it does not exist")
    public ResponseEntity<DogResponseDTO> saveNewDog(@ModelAttribute CreateDogDTO createDogDTO) {

        try {
            String pathToDir = new RandomMethods().createFilePath(createDogDTO.getPrimaryPic().getOriginalFilename());
            File path = new File(pathToDir);
            boolean hasSavedFile = path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(createDogDTO.getPrimaryPic().getBytes());
            output.close();

            DogModel dogToSave = objectMapper.map(createDogDTO, DogModel.class);
            ApplicationUser creator = new RandomMethods().getLoggedUser(authService);
            dogToSave.setCreatedBy(creator.getId());
            dogToSave.setPrimaryPic(pathToDir);
            dogToSave.setRegDate(new Date());

            DogModel savedDog = dogService.saveDogModel(dogToSave, path);
            log.info(savedDog.toString());

            return ResponseEntity.status(201).body(objectMapper.map(savedDog
                    , DogResponseDTO.class));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogResponseFullDTO> getSpecificDog(@PathVariable Long id) {
        DogModel dogModel = dogService.findDogModelByIdAndDeleted(id);
        ApplicationUser dogOwner = authService.getUser(dogModel.getCreatedBy());
        DogResponseFullDTO dogResponseFullDTO = objectMapper.map(dogModel, DogResponseFullDTO.class);
        dogResponseFullDTO.setOwner(objectMapper.map(dogOwner, AttachmentUserProfileFull.class));
        return ResponseEntity.ok().body(dogResponseFullDTO);
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
