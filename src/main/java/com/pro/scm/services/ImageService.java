package com.pro.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file, String fileName);

    String getURLFromPublicId(String publicId);
}
