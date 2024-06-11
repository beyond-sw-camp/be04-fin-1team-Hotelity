package org.iot.hotelitybackend.employee.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    private final EmployeeRepository employeeRepository;

    private final ModelMapper mapper;

    public String upload(MultipartFile image, int employCode) {
        if (!validateFile(image)) {
            return null;
        }

        try {
            String imgUrl = uploadImageToS3(image);
            EmployeeEntity selectedEmployee = employeeRepository.findById(employCode).orElse(null);

            if (imgUrl == null || selectedEmployee == null) {
                return null;
            }

            selectedEmployee.setEmployeeProfileImageLink(imgUrl);
            employeeRepository.save(selectedEmployee);

            return imgUrl;
        } catch (Exception e) {
            log.warn(e.toString());
            return null;
        }
    }

    private boolean validateFile(MultipartFile image) {
        if (image.isEmpty() || image.getOriginalFilename() == null) {
            return false;
        }

        String extension = getFileExtension(image.getOriginalFilename());
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        try (InputStream is = image.getInputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(is))) {

            String originalFilename = image.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String s3FileName = generateS3FileName(originalFilename);

            ObjectMetadata metadata = createObjectMetadata(extension, byteArrayInputStream.available());
            PutObjectRequest putObjectRequest = createPutObjectRequest(s3FileName, byteArrayInputStream, metadata);
            amazonS3.putObject(putObjectRequest);

            return amazonS3.getUrl(bucketName, s3FileName).toString();
        } catch (IOException e) {
            log.warn(e.toString());
            return null;
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }

        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private String generateS3FileName(String originalFilename) {
        return UUID.randomUUID().toString().substring(0, 10) + originalFilename;
    }

    private ObjectMetadata createObjectMetadata(String extension, int contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(contentLength);
        return metadata;
    }

    private PutObjectRequest createPutObjectRequest(String s3FileName, ByteArrayInputStream byteArrayInputStream, ObjectMetadata metadata) {
        return new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
    }

    public boolean deleteImageFromS3(String imageAddress, int employCode) {
        String key = getKeyFromImageAddress(imageAddress);

        try {
            EmployeeEntity selectedEmployee = employeeRepository.findById(employCode).orElse(null);

            if (selectedEmployee == null) {
                return false;
            }

            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));

            selectedEmployee.setEmployeeProfileImageLink(null);
            employeeRepository.save(selectedEmployee);
        } catch (Exception e) {
            log.warn(e.toString());
            return false;
        }

        return true;
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1); // Remove leading '/'
        } catch (MalformedURLException e) {
            log.warn(e.toString());
            return null;
        }
    }

}
