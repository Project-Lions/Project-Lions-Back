package com.project_lions.back.domain;

import com.project_lions.back.domain.common.BaseEntity;
import com.project_lions.back.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

  //primary Key
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //email
  @Column(nullable = false, length = 30, unique = true)
  private String email;

  //비밀번호
  @Column(nullable = false)
  private String password;

  //이름
  @Column(nullable = false)
  private String name;

  //전화번호
  @Column(nullable = false)
  private String phone;

  //주소
  @Column(nullable = false)
  private String address;

  //권한 -> USER, ADMIN
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;


  private String refreshToken; //리프레시 토큰

  //== 정보 수정 ==//
  public void update(String name, String phone, String address) {
    this.name = name;
    this.phone = phone;
    this.address = address;
  }

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void destroyRefreshToken() {
    this.refreshToken = null;
  }
}
