package com.project_lions.back.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {

    private Long reviewId;
    private String uesrname;
    private String shopName;
    private String title;
    private String body;
    private String image;

}
