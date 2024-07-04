package com.luxtix.eventManagementWebsite.cloudinary;
import com.cloudinary.Cloudinary;

import com.luxtix.eventManagementWebsite.exceptions.DataNotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {


    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String foldername) {
        try{
            byte[] bytes = file.getBytes();
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", foldername);
            Map uploadedFile = cloudinary.uploader().upload(bytes, options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        }catch (IOException e){
            throw new DataNotFoundException("Cloudinary not found");

        }
    }
}
