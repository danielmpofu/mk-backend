package com.example.demo.controller;

import com.example.demo.entities.MediaFile;
import com.example.demo.payload.request.CreateMediaFileDTO;
import com.example.demo.payload.request.MediaFileUpdateDTO;
import com.example.demo.payload.response.MediaFileResponseDTO;
import com.example.demo.service.MediaFileService;
import com.example.demo.util.RandomMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/media/")
public class MediaFileController {
    private final MediaFileService mediaFileService;
    private final ModelMapper modelMapper;

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MediaFileResponseDTO>> getAllMediaFiles(@PathVariable long ownerId) {
        List<MediaFile> mediaFileList = mediaFileService.findForOwner(ownerId);

        return ResponseEntity
                .ok()
                .body(mediaFileList
                        .stream()
                        .parallel()
                        .map(mediaFile -> {
                            MediaFileResponseDTO responseDTO = modelMapper.map(mediaFile, MediaFileResponseDTO.class);
                            responseDTO.setDownloadUrl(String.format("/owner/%s", mediaFile.getId()));
                            responseDTO.setMime(new RandomMethods().detectMimetype(new File(mediaFile.getUploadPath())));
                            return responseDTO;
                        })
                        .collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity<MediaFileResponseDTO> saveNewMediaFile(@RequestBody CreateMediaFileDTO createMediaFileDTO) {
        throw new NotImplementedException();
    }

    @GetMapping("/filename/")
    public ResponseEntity<Resource> getByFileName(@RequestParam("fileName") String fileName) throws FileNotFoundException {
        MediaFile mediaFile = mediaFileService.findMediaFileByUploadPath(fileName);
        return new RandomMethods().serveFile(mediaFile);
    }

    @GetMapping("/fileid/{fileId}")
    public ResponseEntity<Resource> getByFileId(@PathVariable long fileId) throws FileNotFoundException {
        MediaFile mediaFile = mediaFileService.getMediaFile(fileId);
        return new RandomMethods().serveFile(mediaFile);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MediaFileResponseDTO> getSpecificMediaFile(@PathVariable Long id) {
        throw new NotImplementedException();
    }

    @PutMapping()
    public ResponseEntity<MediaFileResponseDTO> updateMediaFile(@RequestBody MediaFileUpdateDTO MediaFileUpdateDTO) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaFile(@PathVariable Long id) {
        throw new NotImplementedException();
    }

}
