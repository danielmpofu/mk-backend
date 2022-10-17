package com.example.demo.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MediaFile extends Auditable {
    @Setter
    @Getter
    private @Id
    @GeneratedValue
    @Column(nullable = false) Long id;

    @Setter
    @Getter
    private String uploadPath,mime, fileName, fileExtension, filePurpose;
    @Setter
    @Getter
    private Long fileSize, fileOwner, dateUploaded;

}