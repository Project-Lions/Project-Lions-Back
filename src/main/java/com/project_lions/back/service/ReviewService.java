package com.project_lions.back.service;

import com.project_lions.back.apiPayload.code.status.ErrorStatus;
import com.project_lions.back.apiPayload.exception.handler.MemberHandler;
import com.project_lions.back.apiPayload.exception.handler.ReviewHandler;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.Review;
import com.project_lions.back.domain.Shop;
import com.project_lions.back.domain.dto.ReviewRequestDto;
import com.project_lions.back.domain.dto.ReviewResponseDto;
import com.project_lions.back.repository.MemberRepository;
import com.project_lions.back.repository.ReviewRepository;
import com.project_lions.back.repository.ShopRepository;
import com.project_lions.back.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final ShopService shopService;

    /*
    listReviews
    detailReview
    createReview - 으앙
    updateReview
    deleteReview
    */

    public ResponseEntity<?> listReviews(Long shopId){

        // 소품샵 존재하는지 확인 - 예외처리
        Shop shop = shopRepository.findById(shopId).orElseThrow(()
                -> new ReviewHandler(ErrorStatus.SHOP_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByShop(shop);

        List<ReviewResponseDto> responses = reviews.stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .uesrname(review.getMember().getName())
                        .shopName(review.getShop().getShopName())
                        .title(review.getTitle())
                        .body(review.getBody())
                        .image(review.getImage())
                        .build())
                .toList();

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> detailReview(Long reviewId){

        // 리뷰 존재하는지 확인 - 예외처리
        Review review = reviewRepository.findById(reviewId).orElseThrow(()
                -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));

        ReviewResponseDto response = ReviewResponseDto.builder()
                .reviewId(review.getId())
                .uesrname(review.getMember().getName())
                .shopName(review.getShop().getShopName())
                .title(review.getTitle())
                .body(review.getBody())
                .image(review.getImage())
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> createReview(Long shopId, ReviewRequestDto reviewRequestDto, MultipartFile image) {

        // 현재 접속 멤버 존재하는지 확인 - 예외처리
        Member user = memberRepository.findByEmail(
                SecurityUtil.getLoginUsername()).orElseThrow(()
                -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId).orElseThrow(()
                    -> new ReviewHandler(ErrorStatus.SHOP_NOT_FOUND));

        String reviewImage = s3Service.saveFile(image);

        // 위도 경도 확인 - 소품샵 근처가 아니면 예외처리
        if(!shopService.findShopWithMyLocation(reviewRequestDto.getLatitude(),
                reviewRequestDto.getLongitude()).contains(shop)){
            throw new ReviewHandler(ErrorStatus.REVIEW_NOT_PLACE);
        }

        Review review = Review.builder()
                .member(user)
                .shop(shop)
                .title(reviewRequestDto.getTitle())
                .body(reviewRequestDto.getBody())
                .image(reviewImage)
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDto response = ReviewResponseDto.builder()
                .reviewId(savedReview.getId())
                .uesrname(savedReview.getMember().getName())
                .shopName(savedReview.getShop().getShopName())
                .title(savedReview.getTitle())
                .body(savedReview.getBody())
                .image(savedReview.getImage())
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> updateReview(Long shopId,ReviewRequestDto reviewRequestDto, MultipartFile image) {

        Member user = memberRepository.findByEmail(
                SecurityUtil.getLoginUsername()).orElseThrow(()
                -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId).orElseThrow(()
                -> new ReviewHandler(ErrorStatus.SHOP_NOT_FOUND));

        Review review = reviewRepository.findById(reviewRequestDto.getId()).orElseThrow(()
                -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));

        // 현재 접속자와 작성자 같은지 확인 - 예외처리
        if (!user.equals(review.getMember())){
            throw new MemberHandler(ErrorStatus.MEMBER_INCONSISTENCY);
        }

        String reviewImage = s3Service.saveFile(image);

        review.updateTitle(reviewRequestDto.getTitle());
        review.updateBody(reviewRequestDto.getBody());
        review.updateImage(reviewImage);

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDto response = ReviewResponseDto.builder()
                .reviewId(savedReview.getId())
                .uesrname(savedReview.getMember().getName())
                .shopName(savedReview.getShop().getShopName())
                .title(savedReview.getTitle())
                .body(savedReview.getBody())
                .image(savedReview.getImage())
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteReview(Long reviewId) {

        Member user = memberRepository.findByEmail(
                SecurityUtil.getLoginUsername()).orElseThrow(()
                -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId).orElseThrow(()
                -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));

        if (!user.equals(review.getMember())){
            throw new MemberHandler(ErrorStatus.MEMBER_INCONSISTENCY);
        }

        reviewRepository.deleteById(review.getId());

        return ResponseEntity.ok().build();
    }

}
