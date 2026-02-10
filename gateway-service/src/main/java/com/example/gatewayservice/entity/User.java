package com.example.gatewayservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sellComName; // 사업자명

    @Column(nullable = false)
    private String sellRegNum; // 사업자 등록번호

    @Column(nullable = false)
    private String sellRepName; // 대표자 성명

    @Column(nullable = false)
    private String sellComBirth; // 개업 일자

    @Column(nullable = false)
    private String sellComAdr; // 회사 주소

    @Column(nullable = false)
    private String sellComNum; // 회사 번호

    @Column(nullable = false)
    private String sellBmName; // 담당자 이름

    @Column(nullable = false)
    private String sellBmNum; // 담당자 전화번호

    @Column(nullable = false)
    private String sellBmDep; // 담당자 부서

    @Column(unique = true, nullable = false)
    private String sellComId; // Id

    @Column(nullable = false, length = 32)
    private String salt;

    @Column(nullable = false)
    private String sellComPw; // password

    @Column(nullable = false)
    private String sellComEmail; // Email

    @Column(nullable = false, length = 1)
    private String approvYn = "N"; // 기본값 N

    @Column(nullable = false, length = 1)
    private String marketingCheck;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType; // SELL / BUY

    private String role = "USER";

    @CreationTimestamp // ★ 핵심: INSERT 쿼리가 날아갈 때 자동으로 현재 시간을 넣어줍니다.
    @Column(name = "reg_date", updatable = false) // 정보 수정할 때 이 날짜는 바뀌면 안 되니까 false
    private LocalDateTime regDate;
} 