package com.project_lions.back.controller;

import com.project_lions.back.apiPayload.ApiResponse;
import com.project_lions.back.apiPayload.code.status.SuccessStatus;
import com.project_lions.back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

  private final S3Service s3Service;

  @PostMapping
  public ApiResponse<?> imageUpload(@RequestPart(value = "image", required = false) MultipartFile image){
    String imageUrl = s3Service.saveFile(image);
    return ApiResponse.of(SuccessStatus.IMAGE_UPLOAD, imageUrl);
  }
}
