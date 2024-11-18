package com.project_lions.back.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
public class ShopCreateDTO {


    private String shopName;

    private double latitude;  // 위도

    private double longitude; // 경도

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openAt;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeAt;

    @Builder.Default
    private Set<String> shopTags =  new HashSet<>();
}
