package com.example.seller.mypage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "coperate_members") // DB 테이블명
public class CorporateMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELLER_ID")
    private Long sellerId; // PK

    // --- [기업 정보] ---
    @Column(name = "sell_com_name", nullable = false)
    private String sellComName;   // 회사명

    @Column(name = "sell_reg_num", nullable = false)
    private String sellRegNum;    // 사업자등록번호

    @Column(name = "sell_rep_name", nullable = false)
    private String sellRepName;   // 대표자명

    @Column(name = "sell_com_adr", nullable = false)
    private String sellComAdr;    // 회사 주소

    @Column(name = "sell_com_num", nullable = false)
    private String sellComNum;    // 회사 전화번호

    @Column(name = "sell_com_email", nullable = false)
    private String sellComEmail;  // 회사 이메일

    // --- [계정 정보] ---
    @Column(name = "sell_com_id", nullable = false, unique = true)
    private String sellComId;     // 아이디

    @Column(name = "sell_com_pw", nullable = false)
    private String sellComPw;     // 비밀번호 (변경 대상)

    // --- [담당자 정보] ---
    @Column(name = "sell_bm_name", nullable = false)
    private String sellBmName;    // 담당자명

    @Column(name = "sell_bm_num", nullable = false)
    private String sellBmNum;     // 담당자 연락처

    @Column(name = "sell_bm_dep", nullable = false)
    private String sellBmDep;     // 담당자 부서

    @Column(name = "sell_created_at")
    private LocalDateTime sellCreatedAt;


    // [핵심 기능] 비밀번호 변경 메서드
    public void updatePassword(String newPassword) {
        this.sellComPw = newPassword;
    }
}