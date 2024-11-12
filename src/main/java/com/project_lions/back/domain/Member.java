package com.project_lions.back.domain;

import com.project_lions.back.domain.common.BaseEntity;
import com.project_lions.back.domain.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

  //primary Key
  private Long id;

  //email
  private String email;

  //비밀번호
  private String password;

  //이름
  private String name;

  //전화번호
  private String phone;

  //주소
  private String address;

  //권한 -> USER, ADMIN
  private Role role;
}
