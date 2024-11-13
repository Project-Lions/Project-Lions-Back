package com.project_lions.back.global.login.handler;

import com.project_lions.back.global.jwt.JwtService;
import com.project_lions.back.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final MemberRepository memberRepository;


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    response.setCharacterEncoding("utf-8");

    String username = extractUsername(authentication);
    String accessToken = jwtService.createAccessToken(username);
    String refreshToken = jwtService.createRefreshToken();

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    memberRepository.findByEmail(username).ifPresent(
        member -> member.updateRefreshToken(refreshToken)
    );

    log.info("로그인에 성공합니다. username: {}", username);
    log.info("AccessToken 을 발급합니다. AccessToken: {}", accessToken);
    log.info("RefreshToken 을 발급합니다. RefreshToken: {}", refreshToken);
  }


  private String extractUsername(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }
}