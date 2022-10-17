package com.example.demo.service.impl;

import com.example.demo.entities.MediaFile;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.repo.MediaFileRepository;
import com.example.demo.service.MediaFileService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class MediaFileServiceImpl implements MediaFileService {

    private MediaFileRepository mediaFileRepository;

    @Override
    public MediaFile getMediaFile(long id) {
        return mediaFileRepository
                .findById(id)
                .orElseThrow();
    }

    @Override
    public MediaFile getByFilename(String fileName) {
        return mediaFileRepository.findMediaFileByFileName(fileName);
    }

    @Override
    public MediaFile getByUploadPath(String uploadPath) {
        return mediaFileRepository.findMediaFileByUploadPath(uploadPath);
    }

    @Override
    public MediaFile saveMediaFile(MediaFile mediaFile) {
        return mediaFileRepository.save(mediaFile);
    }

    @Override
    public MediaFile findMediaFileByUploadPath(String uploadPath) {
        return mediaFileRepository.findMediaFileByUploadPath(uploadPath);
    }

    @Override
    public List<MediaFile> findForOwner(long ownerId) {
        return mediaFileRepository.findAllByFileOwner(ownerId)
                .stream()
                .toList();
    }


}
