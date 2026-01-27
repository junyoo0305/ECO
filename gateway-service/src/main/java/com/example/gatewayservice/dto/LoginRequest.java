package com.example.gatewayservice.dto;

import com.example.gatewayservice.entity.CompanyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String sellComId;
    private String sellComPw;
    private CompanyType companyType;
} 