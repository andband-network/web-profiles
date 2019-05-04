package com.andband.profiles.web.profiles;

import com.amazonaws.services.s3.AmazonS3;
import com.andband.profiles.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private AmazonS3 s3client;
    private String bucketName;

    private static final String PROFILE_PLACEHOLDER_IMAGE = "image/profile-placeholder.png";

    ImageService(@Qualifier("profileImageBucket") AmazonS3 s3client,
                 @Value("${andband.aws.s3.profile-image.bucket-name}") String bucketName) {
        this.s3client = s3client;
        this.bucketName = bucketName;
    }

    String createProfileImagePlaceholder() {
        String imageId = UUID.randomUUID().toString();
        File profilePlaceHolderImage = getFileFromClasspath(PROFILE_PLACEHOLDER_IMAGE);
        uploadImage(profilePlaceHolderImage, imageId);
        deleteFile(profilePlaceHolderImage);
        return imageId;
    }

    private File getFileFromClasspath(String location) {
        ClassPathResource resource = new ClassPathResource(location);
        try {
            String outputFile = System.getProperty("java.io.tmpdir") + "profile-image.png";
            Files.copy(resource.getInputStream(), Paths.get(outputFile));
            return new File(outputFile);
        } catch (IOException e) {
            throw new ApplicationException("error retrieving file from classpath", e);
        }
    }

    void uploadImage(File image, String imageId) {
        s3client.putObject(bucketName, imageId, image);
    }

    void uploadImage(MultipartFile multipartFile, String imageId) {
        File image = convert(multipartFile);
        s3client.putObject(bucketName, imageId, image);
        deleteFile(image);
    }

    private void deleteFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new ApplicationException("error deleting file" + file.getName(), e);
        }
    }

    private File convert(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("profile-image", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (IOException | NullPointerException e) {
            throw new ApplicationException("error converting multipart file : " + multipartFile.getOriginalFilename());
        }
    }

}
