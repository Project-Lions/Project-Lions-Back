package com.project_lions.back.repository;

import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Member save(MemberRequestDto.SignUp memberSignUpDto);
  boolean existsByEmail(String email);
}
