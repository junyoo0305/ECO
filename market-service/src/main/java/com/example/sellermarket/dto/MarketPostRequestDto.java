package com.example.sellermarket.dto;

import com.example.sellermarket.model.MarketPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MarketPostRequestDto {
    private String title;
    private String energyType;
    private String landType;
    private Double landArea;
    private String location;
    private String locationDetail;
    private Integer facilityCapacity;
    private BigDecimal weightingFactor;
    private Long volumeKwh;
    private Integer volumeRec;
    private String contractType;
    private String contractUnit;
    private Long priceKrw;
    private String isPriceNegotiable;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String isPeriodNegotiable;
    private String content;

    private MultipartFile imageFile; // 파일 받는 필드

    public MarketPost toEntity(Long sellerId) {
        return MarketPost.builder()
                .sellerId(sellerId)
                .title(this.title)
                .energyType(this.energyType)

                // 추가 정보들
                .landType(this.landType)
                .landArea(this.landArea)
                .location(this.location)
                .locationDetail(this.locationDetail)
                .facilityCapacity(this.facilityCapacity)
                .weightingFactor(this.weightingFactor)
                .volumeKwh(this.volumeKwh)
                .volumeRec(this.volumeRec)
                .contractType(this.contractType)
                .contractUnit(this.contractUnit)

                .priceKrw(this.priceKrw)
                .isPriceNegotiable("on".equals(this.isPriceNegotiable) ? "Y" : "N")

                .contractStartDate(this.contractStartDate)
                .contractEndDate(this.contractEndDate)
                .isPeriodNegotiable("on".equals(this.isPeriodNegotiable) ? "Y" : "N")

                .content(this.content)

                .saleYn("Y")
                .delYn("N")
                .regDate(LocalDateTime.now())
                .build();
    }
}