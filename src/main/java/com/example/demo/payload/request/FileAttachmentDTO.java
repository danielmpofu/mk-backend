package com.example.demo.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Data
public class FileAttachmentDTO {
    private String fileName;
    private String fileType;
    private String filePath;
    private String originalFilename;
    private MultipartFile file;
    private Long ownerId;

}
