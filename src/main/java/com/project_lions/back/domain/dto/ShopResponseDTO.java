package com.project_lions.back.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.project_lions.back.domain.Tag;
import com.project_lions.back.domain.enums.Like;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class ShopResponseDTO {

    @Data
    @Builder
    @Getter
    public static class ShopLikeResponseDto {

        private String shopName;

        private double latitude;  // 위도

        private double longitude; // 경도

        @JsonFormat(pattern = "HH:mm")
        private LocalTime openAt; //오픈시간

        @JsonFormat(pattern = "HH:mm")
        private LocalTime closeAt;//마감시간

        private String image;
    }

    @Data
    @Builder
    @Getter
    public static class ShopFindResponseDTO {

        private String shopName;

        private String ownerPhone;

        private double latitude;  // 위도

        private double longitude; // 경도

        private String image;

        @Builder.Default
        private Set<Tag> shopTags =  new HashSet<>();
    }

    @Data
    @Builder
    @Getter
    public static class ShopDetailsResponseDTO{
        private Long id;

        private String image;

        private String shopName;

        private String ownerPhone;

        private double latitude;  // 위도

        private double longitude; // 경도

        @JsonFormat(pattern = "HH:mm")
        private LocalTime openAt; //오픈시간

        @JsonFormat(pattern = "HH:mm")
        private LocalTime closeAt;//마감시간

        @Enumerated(EnumType.STRING)
        private Like likeShop;

        @Builder.Default
        private Set<Tag> shopTags =  new HashSet<>();

        private String description; //소개글

    }


}