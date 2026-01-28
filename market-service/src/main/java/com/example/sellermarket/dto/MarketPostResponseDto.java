package com.example.sellermarket.dto;

import com.example.sellermarket.model.MarketPost;
import com.example.sellermarket.model.PostFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketPostResponseDto {
    private Long id;
    private Long sellerId;
    private String title;
    private String content;
    private String energyType;

    // 부지 정보
    private String landType;
    private Double landArea;
    private String location;
    private String locationDetail;

    // 설비 정보
    private Integer facilityCapacity;
    private BigDecimal weightingFactor;
    private Long volumeKwh;
    private Integer volumeRec;

    // 계약 정보
    private String contractType;
    private String contractUnit;

    // 가격 및 기간
    private Long priceKrw;
    private String isPriceNegotiable;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String isPeriodNegotiable;

    // 상태 및 이미지
    private String status;
    private LocalDateTime regDate;
    private String imageUrl;

    public static MarketPostResponseDto fromEntity(MarketPost entity) {

        // 삭제되지 않은('N') 파일 중 첫 번째 사진만 가져오기
        String thumbUrl = null;
        List<PostFile> files = entity.getFiles();
        if (files != null && !files.isEmpty()) {
            thumbUrl = files.stream()
                    // 1. 삭제 여부(delYn)가 'N'인 것만 필터링
                    .filter(file -> "N".equals(file.getDelYn()))
                    // 2. 그 중 첫 번째 파일 선택
                    .findFirst()
                    // 3. 경로 문자열로 변환
                    .map(file -> "/market/images/" + file.getStoredFileName())
                    // 4. 없으면 null
                    .orElse(null);
        }

        return MarketPostResponseDto.builder()
                .id(entity.getPostId())
                .sellerId(entity.getSellerId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .energyType(entity.getEnergyType())
                .landType(entity.getLandType())
                .landArea(entity.getLandArea())
                .location(entity.getLocation())
                .locationDetail(entity.getLocationDetail())
                .facilityCapacity(entity.getFacilityCapacity())
                .weightingFactor(entity.getWeightingFactor())
                .volumeKwh(entity.getVolumeKwh())
                .volumeRec(entity.getVolumeRec())
                .contractType(entity.getContractType())
                .contractUnit(entity.getContractUnit())
                .priceKrw(entity.getPriceKrw())
                .isPriceNegotiable(entity.getIsPriceNegotiable())
                .contractStartDate(entity.getContractStartDate())
                .contractEndDate(entity.getContractEndDate())
                .isPeriodNegotiable(entity.getIsPeriodNegotiable())
                .regDate(entity.getRegDate())

                .imageUrl(thumbUrl)

                .status("Y".equals(entity.getSaleYn()) ? "판매중" : "판매종료")
                .build();
    }
}