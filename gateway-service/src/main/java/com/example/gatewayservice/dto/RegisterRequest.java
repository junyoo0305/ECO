package com.example.gatewayservice.dto;

import com.example.gatewayservice.entity.CompanyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private CompanyType companyType; // Buy/Sell 구분
    private String sellComName; // 사업자명
    private String sellRegNum; // 사업자 등록번호
    private String sellRepName; // 대표자 성명
    private String sellComBirth; // 개업일자
    private String sellComAdr; // 회사 주소
    private String sellComNum; // 회사 전화번호
    private String sellBmName; // 담당자 이름
    private String sellBmNum; // 담당자 전화번호
    private String sellBmDep; // 담당자 부서
    private String sellComId; // Id
    private String salt;
    private String sellComPw; // Password
    private String sellComPwConfirm; // ✅ 비밀번호 확인
    private String sellComEmail; // Email
    private String marketingCheck; // 마케팅 이용약관 체크 유무
} 