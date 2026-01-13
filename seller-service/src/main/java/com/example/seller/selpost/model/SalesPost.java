package com.example.seller.selpost.model;
//import com.example.seller.model.PostFile; // PostFile import 필요
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@DynamicInsert
@Table(name = "sales_posts")
public class SalesPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "SELLER_ID", nullable = false)
    private Long sellerId;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "ENERGY_TYPE", nullable = false, length = 50)
    private String energyType;

    @Column(name = "LAND_TYPE")
    private String landType;

    @Column(name = "LAND_AREA")
    private Double landArea;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "LOCATION_DETAIL")
    private String locationDetail;

    @Column(name = "FACILITY_CAPACITY")
    private Integer facilityCapacity;

    @Column(name = "VOLUME_KWH")
    private Long volumeKwh;

    @Column(name = "VOLUME_REC")
    private Integer volumeRec;

    @Column(name = "WEIGHTING_FACTOR")
    @ColumnDefault("1.0")
    private BigDecimal weightingFactor;

    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    @Column(name = "CONTRACT_UNIT")
    private String contractUnit;

    @Column(name = "PRICE_KRW")
    private Long priceKrw;

    @Column(name = "IS_PRICE_NEGOTIABLE")
    @ColumnDefault("'N'")
    private String isPriceNegotiable;

    @Column(name = "CONTRACT_START_DATE")
    private LocalDate contractStartDate;

    @Column(name = "CONTRACT_END_DATE")
    private LocalDate contractEndDate;

    @Column(name = "IS_PERIOD_NEGOTIABLE")
    @ColumnDefault("'N'")
    private String isPeriodNegotiable;

    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    // JOIN을 통한 이미지파일 가져오기
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID") // PostFile 테이블의 POST_ID와 연결
    private List<PostFile> files = new ArrayList<>();

    @Column(name = "SALE_YN")
    @ColumnDefault("'Y'")
    private String saleYn;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "DEL_YN")
    @ColumnDefault("'N'")
    private String delYn;
}