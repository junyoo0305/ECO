package com.example.seller.util;

import org.springframework.stereotype.Component; // ★ 필수 import
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component // 스프링이 관리하는 객체(Bean) 등록
public class Sha512Util {

    private final SecureRandom random = new SecureRandom();

    // Salt 생성
    public String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 암호화 (Service 코드와 이름 일치시킴: hash -> encrypt)
    public String encrypt(String sellComPw, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // 솔트와 비밀번호 결합 방식이 기존과 같다면 유지
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(sellComPw.getBytes(StandardCharsets.UTF_8));

            // Hex 인코딩 (기존 코드 유지)
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("암호화 오류", e);
        }
    }
}