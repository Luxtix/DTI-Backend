package com.luxetix.eventManagementWebsite.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String uploadFile(MultipartFile file, String foldername);
}
