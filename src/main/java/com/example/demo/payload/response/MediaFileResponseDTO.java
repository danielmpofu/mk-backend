package com.example.demo.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MediaFileResponseDTO {
    @Setter @Getter
    private String fileName, fileExtension, mime, uploadPath, downloadUrl;
    @Setter @Getter
    private long fileId, fileOwner, fileSize;
    @Setter @Getter
    private Date dateUploaded;
}
