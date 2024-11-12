package com.project_lions.back.service;

import com.project_lions.back.apiPayload.code.status.ErrorStatus;
import com.project_lions.back.apiPayload.exception.handler.MemberHandler;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberRequestDto;
import com.project_lions.back.repository.MemberRepository;
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
        return memberRepository.save(memberSignUpDto);
    }
}
