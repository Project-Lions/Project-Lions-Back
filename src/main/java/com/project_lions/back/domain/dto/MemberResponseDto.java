package com.project_lions.back.domain.dto;

import lombok.Builder;
import lombok.Data;

public class MemberResponseDto {

  @Data
  @Builder
  public static class LoginResultDto {

    private String accessToken;
    private String refreshToken;
  }

  @Data
  @Builder
  public static class SignUpDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private String address;
  }

  @Data
  @Builder
  public static class UpdateDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private String address;
  }
}
