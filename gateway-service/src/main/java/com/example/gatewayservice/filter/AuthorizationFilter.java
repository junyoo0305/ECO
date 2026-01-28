package com.example.gatewayservice.filter;

import com.example.gatewayservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 인증 제외 경로 (로그인, 회원가입, 정적 리소스 등)
        if (isExcludePath(path)) {
            return chain.filter(exchange);
        }

        String token = null;
        ServerHttpRequest request = exchange.getRequest();

        // 헤더에서 토큰 찾기 (API 요청)
        if (request.getHeaders().containsKey("Authorization")) {
            String headerToken = request.getHeaders().getFirst("Authorization");
            if (headerToken != null && headerToken.startsWith("Bearer ")) {
                token = headerToken.substring(7);
            }
        }

        // 쿠키에서 토큰 찾기 (화면 이동)
        if (token == null && request.getCookies().containsKey("Authorization")) {
            HttpCookie cookie = request.getCookies().getFirst("Authorization");
            if (cookie != null) {
                // 쿠키 값 디코딩 (Bearer%20... -> Bearer ...)
                String rawValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if (rawValue.startsWith("Bearer ")) {
                    token = rawValue.substring(7);
                } else {
                    token = rawValue;
                }
            }
        }

        // 토큰 검증 및 권한 확인
        if (token != null && !jwtUtil.isTokenExpired(token)) {
            try {
                String userId = jwtUtil.extractClaim(token, claims -> claims.getSubject());

                // 토큰에 있는 role ("SELLER" or "BUYER" or "ADMIN")을 꺼냄
                String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));

                // /seller 경로는 "SELLER" 권한만 통과
                if (path.startsWith("/seller") && !"SELLER".equals(role)) {
                    log.warn("⛔ 권한 없음: 사용자({})의 역할({})로는 접근 불가", userId, role);
                    return onError(exchange, "Access denied", HttpStatus.FORBIDDEN);
                }

                // 하위 서비스로 정보 전달
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-USER-ID", userId)
                        .header("X-USER-ROLE", role)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                log.error("Token error", e);
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        }

        // 토큰이 없거나 만료된 경우 -> 로그인 페이지로 리다이렉트
        log.info("❌ 인증 실패 또는 토큰 없음 (Path: {})", path);
        // API 요청이 아니라면 로그인 페이지로 리다이렉트
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(java.net.URI.create("/login"));
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위를 높여서 가장 먼저 실행되도록 함
    }

    // 인증 제외 경로
    private boolean isExcludePath(String path) {
        return path.startsWith("/login") || path.startsWith("/register") ||
                path.startsWith("/css") || path.startsWith("/js") ||
                path.startsWith("/images") || path.startsWith("/h2-console") ||
                path.startsWith("/api/auth") || path.equals("/favicon.ico");
    }
}