package com.ttokttak.jellydiary.user.handler;

import com.ttokttak.jellydiary.jwt.JWTUtil;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${redirect.url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User => Authentication 객체로부터 CustomOAuth2User를 추출
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customUserDetails.getUserId();
        String userName = customUserDetails.getName();
        String authority = customUserDetails.getAuthorities().iterator().next().getAuthority();

        // 토큰 생성 (중간페이지를 만들어서 그곳으로 리다이렉트후 액세스토큰 발급)
        String refreshToken = jwtUtil.createRefreshJwt("refresh", userId, userName, authority);

        // Refresh 토큰 저장
        refreshTokenService.addRefreshTokenEntity(userId, userName, refreshToken);

        // 응답 설정 후 클라이언트로 전송
        ResponseCookie refreshTokenCookie = jwtUtil.createCookie("refresh", refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(redirectUrl);
    }
}
