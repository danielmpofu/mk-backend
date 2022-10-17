package com.example.demo.service;

import com.example.demo.entities.MediaFile;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MediaFileService {
    MediaFile getMediaFile(long id);

    MediaFile getByFilename(String fileName);
    MediaFile getByUploadPath(String uploadPath);

    MediaFile saveMediaFile(MediaFile mediaFile);

    MediaFile findMediaFileByUploadPath(String uploadPath);

    List<MediaFile> findForOwner(long ownerId);


}
