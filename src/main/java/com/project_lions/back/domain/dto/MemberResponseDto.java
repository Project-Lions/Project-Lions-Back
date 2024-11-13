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
  }
}
