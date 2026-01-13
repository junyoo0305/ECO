// 검색 필터 기능
package com.example.market.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarketPostSearchDto {
    private List<String> contractTypes; // 계약 유형 (중복 선택 가능)
    private List<String> energyTypes;   // 발전원
    private List<String> regions;       // 생산 지역
    private Integer minPrice;           // 최소 가격
    private Integer maxPrice;           // 최대 가격
    private Boolean isNego;             // 협의 가능만 보기 여부
}