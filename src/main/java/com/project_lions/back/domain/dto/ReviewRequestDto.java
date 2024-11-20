package com.project_lions.back.domain.dto;

import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRequestDto {

    private Long id;
    private String title;
    private String body;
    private String image;

    @Nullable
    private Double latitude;
    private Double longitude;
}

