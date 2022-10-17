package com.example.demo.repo;

import com.example.demo.entities.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    MediaFile findMediaFileByFileName(String fileName);

    MediaFile findMediaFileByUploadPath(String filePath);

    Collection<MediaFile> findAllByFileOwner(long ownerId);
}