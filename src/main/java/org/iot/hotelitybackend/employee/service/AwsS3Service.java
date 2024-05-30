package org.iot.hotelitybackend.employee.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    String upload(MultipartFile image, int employCode);

    boolean deleteImageFromS3(String addr, int employCode);
}
