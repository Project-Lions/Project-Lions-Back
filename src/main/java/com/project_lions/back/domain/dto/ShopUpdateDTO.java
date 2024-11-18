package com.project_lions.back.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Set;

@Builder
@Getter
@Setter
public class ShopUpdateDTO {


    private String shopName;

    private String ownerPhone;

    private String ownerName;

    private double latitude;  // 위도

    private double longitude; // 경도

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openAt; //오픈시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeAt;//마감시간

    private Set<String> shopTags;

}