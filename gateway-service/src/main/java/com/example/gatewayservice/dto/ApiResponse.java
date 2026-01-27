package com.example.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success; // 성공 여부
    private T data;          // 성공 시 데이터
    private String message;  // 실패 시 메시지
}
