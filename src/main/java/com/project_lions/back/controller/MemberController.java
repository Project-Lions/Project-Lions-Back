package com.project_lions.back.controller;

import com.project_lions.back.apiPayload.ApiResponse;
import com.project_lions.back.apiPayload.code.status.SuccessStatus;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberRequestDto;
import com.project_lions.back.domain.dto.MemberResponseDto;
import com.project_lions.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    return ApiResponse.of(SuccessStatus.MEMBER_JOIN, MemberResponseDto.SignUpDto.builder()
        .id(savedMember.getId())
        .email(savedMember.getEmail())
        .name(savedMember.getName())
        .phone(savedMember.getPhone())
        .address(savedMember.getAddress())
        .build());
  }

  @PutMapping
  public ApiResponse<?> update(@RequestBody @Validated MemberRequestDto.Update memberUpdateDto) {
    Member updatedMember = memberService.update(memberUpdateDto);
    return ApiResponse.of(SuccessStatus.MEMBER_JOIN, MemberResponseDto.UpdateDto.builder()
        .id(updatedMember.getId())
        .email(updatedMember.getEmail())
        .name(updatedMember.getName())
        .phone(updatedMember.getPhone())
        .address(updatedMember.getAddress())
        .build());
  }

  @GetMapping
  public ApiResponse<?> myInfo() {
    Member myInfo = memberService.findMy();
    return ApiResponse.of(SuccessStatus.MEMBER_JOIN, MemberResponseDto.MyInfoDto.builder()
        .id(myInfo.getId())
        .email(myInfo.getEmail())
        .name(myInfo.getName())
        .phone(myInfo.getPhone())
        .address(myInfo.getAddress())
        .build());
  }
}
