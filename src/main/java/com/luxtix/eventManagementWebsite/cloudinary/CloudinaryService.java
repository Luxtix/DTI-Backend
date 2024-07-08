package com.luxtix.eventManagementWebsite.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFile(MultipartFile file, String foldername);

    String generateUrl(String publicId);
}
