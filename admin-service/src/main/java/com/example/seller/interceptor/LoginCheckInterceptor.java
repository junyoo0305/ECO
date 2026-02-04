package com.example.seller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 세션에 회원 정보가 없으면 로그인 페이지로 튕겨냄
        if (session == null || session.getAttribute("loginAdmin") == null) {
            response.sendRedirect("/login");
            return false; // 더 이상 진행하지 않음
        }

        return true; // 통과
    }
}
