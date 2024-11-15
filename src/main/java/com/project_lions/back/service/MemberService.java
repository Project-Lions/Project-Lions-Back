package com.project_lions.back.service;

import com.project_lions.back.apiPayload.code.status.ErrorStatus;
import com.project_lions.back.apiPayload.exception.handler.MemberHandler;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberRequestDto;
import com.project_lions.back.domain.enums.Role;
import com.project_lions.back.repository.MemberRepository;
import com.project_lions.back.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member save(MemberRequestDto.SignUp memberSignUpDto) {
    if (memberRepository.existsByEmail(memberSignUpDto.getEmail())) {
      throw new MemberHandler(ErrorStatus.MEMBER_EMAIL_ALREADY_EXIST);
    }
    memberSignUpDto.setPassword(passwordEncoder.encode(memberSignUpDto.getPassword()));
    return memberRepository.save(Member.builder()
        .email(memberSignUpDto.getEmail())
        .password(memberSignUpDto.getPassword())
        .name(memberSignUpDto.getName())
        .phone(memberSignUpDto.getPhone())
        .address(memberSignUpDto.getAddress())
        .role(Role.USER)
        .build());
  }

  public Member update(MemberRequestDto.Update memberUpdateDto) {
    Member member = memberRepository.findByEmail(SecurityUtil.getLoginUsername())
        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

    member.update(memberUpdateDto.getName(), memberUpdateDto.getPhone(), memberUpdateDto.getAddress());
    return member;
  }

  public Member findMy() {
    Member member = memberRepository.findByEmail(SecurityUtil.getLoginUsername())
        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

    return member;
  }
}
