package com.rendi.RendiBackend.auth.service;

import com.rendi.RendiBackend.auth.domain.*;
import com.rendi.RendiBackend.auth.dto.TokenDto;
import com.rendi.RendiBackend.auth.dto.TokenDtoCookie;
import com.rendi.RendiBackend.auth.exception.AuthErrorCode;
import com.rendi.RendiBackend.auth.exception.AuthException;
import com.rendi.RendiBackend.repositories.RefreshTokenRepository;
import com.rendi.RendiBackend.common.AppProperties;
import com.rendi.RendiBackend.member.domain.Role;
import com.rendi.RendiBackend.member.dto.LoginRequest;
import com.rendi.RendiBackend.repositories.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppProperties appProperties;
    private final AuthenticationManager authenticationManager;
    private static final long THREE_DAYS_MSEC = 259200000;

    //TODO: 리팩토링-중복코드 제거

    // 액세스 토큰 발급
    @Transactional
    public AuthToken createAuth(LoginRequest request) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Date now = new Date();
            String username = request.getUsername();

            AuthToken accessToken = tokenProvider.createAuthToken(
                    username,
//                    ((UserAdapter)authentication.getPrincipal()).getMember().getRoleKey(),
                    ((CustomUserDetails)authentication.getPrincipal()).getRole().getKey(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            return accessToken;
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCode.CREDENTIAL_MISS_MATCH);
        }
    }

    // 리프레시 토큰 발급 및 저장 또는 수정
    @Transactional
    public MemberRefreshToken setRefreshToken(LoginRequest request) {
        Date now = new Date();
        String username = request.getUsername();

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token 으로 DB 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsername(username);
        if (memberRefreshToken == null) {
            // 없으면 새로 등록
            memberRefreshToken = new MemberRefreshToken(username, refreshToken.getToken());
            refreshTokenRepository.save(memberRefreshToken);
        } else {
            // DB에 refresh token 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return memberRefreshToken;
    }

    @Transactional
    public AuthInfo reissueToken(TokenDto dto) {
        AuthToken expiredToken = tokenProvider.convertAuthToken(dto.getAccessToken());
        AuthToken refreshToken = tokenProvider.convertAuthToken(dto.getRefreshToken());

        Claims claims = expiredToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new AuthException(AuthErrorCode.NOT_EXPIRED_TOKEN_YET);
        } else {
            log.info("claims={}", claims);
        }
        String username = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        if (!refreshToken.validate()) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token으로 DB에서 user 정보와 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(username, dto.getRefreshToken());
        log.info("UserRefreshToken={}", refreshToken);
        if (memberRefreshToken == null) {
            throw new AuthException(
                    AuthErrorCode.INVALID_REFRESH_TOKEN,
                    "가입되지 않은 회원이거나 유효하지 않은 리프레시 토큰입니다."
            );
        }

        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                username,
                role.getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // 리프레시 토큰 만료기간이 3일 이하인 경우 refresh token 발급
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 토큰 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return new AuthInfo(newAccessToken, memberRefreshToken);
    }
    @Transactional
    public AuthInfo reissueTokenfromCookie(TokenDtoCookie dto, HttpServletRequest request) {
        AuthToken expiredToken = tokenProvider.convertAuthToken(dto.getAccessToken());
        String refreshTokenValue = getRefreshToken(request);
        AuthToken refreshToken = tokenProvider.convertAuthToken(refreshTokenValue);

        Claims claims = expiredToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new AuthException(AuthErrorCode.NOT_EXPIRED_TOKEN_YET);
        } else {
            log.info("claims={}", claims);
        }
        String username = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        if (!refreshToken.validate()) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token으로 DB에서 user 정보와 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(username, refreshTokenValue);
        log.info("UserRefreshToken={}", refreshTokenValue);
        if (memberRefreshToken == null) {
            throw new AuthException(
                    AuthErrorCode.INVALID_REFRESH_TOKEN,
                    "가입되지 않은 회원이거나 유효하지 않은 리프레시 토큰입니다."
            );
        }

        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                username,
                role.getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();
        log.info("validTime ={}", validTime);
        // 리프레시 토큰 만료기간이 3일 이하인 경우 refresh token 발급
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 토큰 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }
        log.info("end");
        return new AuthInfo(newAccessToken, memberRefreshToken);
    }
    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        return decodedValue;
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return "error";
    }



}
