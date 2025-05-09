package com.Harum.Harum.Services;

import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {


    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${CLOUD_NAME}") String cloudName,
            @Value("${API_KEY}") String apiKey,
            @Value("${API_SECRET}") String apiSecret
    ) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString(); // Trả về link ảnh
    }
}
