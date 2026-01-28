package com.example.sellermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PublicCompanyDto {
    // 기업 기본 정보
    private String sellComName;
    private String sellComAdr;
    private String sellComEmail;

    // 주 담당자 정보 (User 테이블)
    private String mainManagerName;
    private String mainManagerNum;
    private String mainManagerDep;

    // 추가 담당자 리스트 (SubManager 테이블)
    private List<SubManagerDto> subManagers;

    @Data
    @AllArgsConstructor
    public static class SubManagerDto {
        private String name;
        private String phone;
        private String email;
        private String department;
    }
}