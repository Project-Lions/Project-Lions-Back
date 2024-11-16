package com.project_lions.back.repository;

import com.project_lions.back.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Member save(Member member);

  boolean existsByEmail(String email);

  boolean existsById(Long id);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByRefreshToken(String refreshToken);

  void deleteByEmail(String email);
}
