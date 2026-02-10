package com.example.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {
    private String key;        // 기준 (날짜, 지역, 발전원, 계약유형)
    private Long totalCount;   // 전체 공고 수
    private Long doneCount;    // 거래 완료 수 (saleYn='N' or purchaseYN='N')
}