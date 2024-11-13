package com.project_lions.back.controller;

import com.project_lions.back.apiPayload.ApiResponse;
import com.project_lions.back.apiPayload.code.status.SuccessStatus;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberRequestDto;
import com.project_lions.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping
  public ApiResponse<?> signUp(@RequestBody @Validated MemberRequestDto.SignUp memberSignUpDto) {
    Member savedMember = memberService.save(memberSignUpDto);
    return ApiResponse.of(SuccessStatus.MEMBER_JOIN, savedMember);
  }
}
