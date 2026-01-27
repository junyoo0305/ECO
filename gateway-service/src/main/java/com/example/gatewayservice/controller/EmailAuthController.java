package com.example.gatewayservice.controller;

import com.example.gatewayservice.util.EmailAuthCode;
import com.example.gatewayservice.util.EmailCodeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth/email")
public class EmailAuthController {

    private final Map<String, EmailAuthCode> emailCodeStore = new ConcurrentHashMap<>();

    private static final int EXPIRE_MINUTES = 5;

    // 이메일 인증 전송
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> send(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        // 새 코드 생성(재인증 시 기존 코드 자동 덮어쓰기
        String code = EmailCodeUtil.generate();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRE_MINUTES);
        emailCodeStore.put(email, new EmailAuthCode(code, expiresAt));

        // 실제 이메일 발송
        System.out.println("EMAIL: " + email + " CODE: " + code);
        // 개발 단계에서만 프론트로 내려줌
        return ResponseEntity.ok(Map.of("code", code));
    }
    // 인증번호 검증
    @PostMapping("/verify")
    public ResponseEntity<Void> verify(
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        EmailAuthCode authCode = emailCodeStore.get(email);
        if (authCode == null) {
            return ResponseEntity.badRequest().build(); // 코드 없음
        }

        //만료 체크
        if (authCode.isExpired()) {
            emailCodeStore.remove(email);
            return ResponseEntity.status(HttpStatus.GONE).build(); // 410
        }

        // 코드 불일치
        if (!authCode.getCode().equals(code)) {
            return ResponseEntity.badRequest().build();
        }

        // 성공 -> 사용 후 제거
        emailCodeStore.remove(email);
        return ResponseEntity.ok().build();
    }
}

