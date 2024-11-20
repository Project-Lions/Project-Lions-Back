package com.project_lions.back.controller;

import com.project_lions.back.domain.dto.ReviewRequestDto;
import com.project_lions.back.domain.dto.ReviewResponseDto;
import com.project_lions.back.domain.dto.ShopCreateDTO;
import com.project_lions.back.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/shops/{shopId}/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //특정 Shop 내에 리뷰 리스트 확인
    @GetMapping("")
    public ResponseEntity<?> listReviews(@PathVariable Long shopId) {
        return reviewService.listReviews(shopId);
    }

    // 리뷰 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> detailReviews(@PathVariable Long id) {
        return reviewService.detailReview(id);
    }

    // 리뷰 작성
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createComment(@PathVariable Long shopId,
                                           @RequestPart(value = "review") @Valid ReviewRequestDto request,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        return reviewService.createReview(shopId, request, image);
    }

    // 리뷰 수정
    @PutMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateComment(@PathVariable Long shopId,
                                           @RequestPart(value = "review") @Valid ReviewRequestDto request,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        return reviewService.updateReview(shopId, request, image);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

}
