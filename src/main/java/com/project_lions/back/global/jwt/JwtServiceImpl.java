package com.project_lions.back.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_lions.back.apiPayload.ApiResponse;
import com.project_lions.back.apiPayload.code.status.ErrorStatus;
import com.project_lions.back.apiPayload.code.status.SuccessStatus;
import com.project_lions.back.apiPayload.exception.handler.MemberHandler;
import com.project_lions.back.domain.Member;
import com.project_lions.back.domain.dto.MemberResponseDto.LoginResultDto;
import com.project_lions.back.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret-key}")
  private String secret;
  @Value("${jwt.access.expiration}")
  private String accessTokenValidityInSeconds;
  @Value("${jwt.refresh.expiration}")
  private String refreshTokenValidityInSeconds;
  @Value("${jwt.access.header}")
  private String accessHeader;
  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String USERNAME_CLAIM = "email";
  private static final String BEARER = "Bearer ";
  private final MemberRepository memberRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String createAccessToken(String username) {
    return JWT.create()
        .withSubject(ACCESS_TOKEN_SUBJECT)
        .withExpiresAt(new Date(
            System.currentTimeMillis() + Long.parseLong(accessTokenValidityInSeconds) * 1000))
        .withClaim(USERNAME_CLAIM, username)
        .sign(Algorithm.HMAC512(secret));
  }

  @Override
  public String createRefreshToken() {
    return JWT.create()
        .withSubject(REFRESH_TOKEN_SUBJECT)
        .withExpiresAt(new Date(
            System.currentTimeMillis() + Long.parseLong(refreshTokenValidityInSeconds) * 1000))
        .sign(Algorithm.HMAC512(secret));
  }

  @Override
  public void updateRefreshToken(String username, String refreshToken) {
    memberRepository.findByEmail(username)
        .ifPresentOrElse(
            member -> member.updateRefreshToken(refreshToken),
            () -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)
        );
  }

  @Override
  public void destroyRefreshToken(String username) {
    memberRepository.findByEmail(username)
        .ifPresentOrElse(
            Member::destroyRefreshToken,
            () -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND)
        );
  }

  @Override
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,
      String refreshToken) throws IOException {
    String result = objectMapper.writeValueAsString(
        ApiResponse.of(
            SuccessStatus.MEMBER_LOGIN,
            LoginResultDto.builder().accessToken(accessToken)
                .refreshToken(refreshToken).build()
        )
    );
    response.getWriter().write(result);
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
  }

  @Override
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenHeader(response, accessToken);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
  }

  @Override
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader)).filter(
        accessToken -> accessToken.startsWith(BEARER)
    ).map(accessToken -> accessToken.replace(BEARER, ""));
  }

  @Override
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader)).filter(
        refreshToken -> refreshToken.startsWith(BEARER)
    ).map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  @Override
  public Optional<String> extractUsername(String accessToken) {
    try {
      return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secret))
          .build()
          .verify(accessToken)
          .getClaim(USERNAME_CLAIM).asString()
      );
    } catch (Exception e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }

  @Override
  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}