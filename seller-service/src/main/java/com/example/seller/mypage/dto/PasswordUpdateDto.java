package com.example.seller.mypage.dto;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String currentPassword;  // 현재 비밀번호
    private String newPassword;      // 새 비밀번호
    private String newPasswordCheck; // 새 비밀번호 확인 (추가됨)
}