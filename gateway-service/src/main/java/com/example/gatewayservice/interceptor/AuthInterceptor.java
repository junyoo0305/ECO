package com.example.gatewayservice.interceptor;

import com.example.gatewayservice.util.JwtUtil;
import jakarta.servlet.http.Cookie; // ★ 쿠키 클래스 import 필수
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // 1. 인증 제외 경로 (로그인, 회원가입, 정적 리소스 등)
        if (path.startsWith("/login") || path.startsWith("/register") ||
                path.startsWith("/css") || path.startsWith("/js") ||
                path.startsWith("/images") || path.startsWith("/h2-console") ||
                path.startsWith("/api/auth")) { // api/auth 경로도 통과시켜야 함
            return true;
        }

        String token = null;

        // 2. 헤더에서 먼저 토큰 찾기 (API 요청인 경우)
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            token = headerToken.substring(7);
        }

        // 3. ★ [추가된 부분] 쿠키에서 토큰 찾기 (화면 이동인 경우)
        // 헤더에 토큰이 없으면 쿠키를 뒤져봅니다.
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // 쿠키 이름이 'Authorization'인 것을 찾음
                if ("Authorization".equals(cookie.getName())) {
                    // 쿠키 값은 URL 인코딩(%20) 되어 있으므로 디코딩
                    String rawValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);

                    if (rawValue.startsWith("Bearer ")) {
                        token = rawValue.substring(7);
                    }
                    break; // 찾았으면 반복 종료
                }
            }
        }

        // 4. 토큰 검증
        if (token != null && !jwtUtil.isTokenExpired(token)) {
            return true; // 통과!
        }

        // 실패 시 로그인 페이지로 튕겨냄
        System.out.println("인증 실패: " + path + " 로 접근했으나 유효한 토큰이 없습니다.");
        response.sendRedirect("/login");
        return false;
    }
}