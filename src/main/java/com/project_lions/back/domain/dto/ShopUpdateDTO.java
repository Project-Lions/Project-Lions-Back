package com.project_lions.back.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Getter
@Setter
public class ShopUpdateDTO {


    private String shopName;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다.")
    private String ownerPhone;

    private String ownerName;

    private double latitude;  // 위도

    private double longitude; // 경도

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openAt; //오픈시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeAt;//마감시간

    private Set<String> shopTags;

    private String description; //소개글

}