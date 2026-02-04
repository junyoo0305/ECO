package com.example.seller.dto;

import com.example.seller.model.Admin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminRequestDto {

    private String userId;     // 화면의 아이디
    private String password;   // 화면의 비밀번호
    private String name;       // 화면의 성명
    private String email;      // 화면의 이메일
    private String department; // 화면의 부서
    private String phone;      // 화면의 전화번호
    private String auth;       // 화면의 권한 (1, 2, 3 등)

    public Admin toEntity() {
        return Admin.builder()
                .userId(this.userId)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .department(this.department)
                .phone(this.phone)
                .auth(this.auth)
                .build();
    }
}
