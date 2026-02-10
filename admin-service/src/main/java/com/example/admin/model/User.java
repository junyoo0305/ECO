package com.example.admin.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "users") // 통합된 테이블명
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK (기존 sellerId 대체)

    // --- [계정 정보] ---
    @Column(name = "sell_com_id", nullable = false, unique = true)
    private String sellComId;     // 아이디

    @Column(name = "sell_com_pw", nullable = false)
    private String sellComPw;     // 비밀번호

    @Column(name = "salt", nullable = false, length = 32)
    private String salt;          // 암호화 Salt

    @Column(name = "role", length = 20)
    @Builder.Default
    private String role = "USER"; // 권한

    // --- [기업 정보] ---
    @Column(name = "sell_com_name", nullable = false)
    private String sellComName;   // 회사명(사업자명)

    @Column(name = "sell_reg_num", nullable = false)
    private String sellRegNum;    // 사업자등록번호

    @Column(name = "sell_rep_name", nullable = false)
    private String sellRepName;   // 대표자명

    @Column(name = "sell_com_birth", nullable = false)
    private String sellComBirth;  // 개업연월일

    @Column(name = "sell_com_adr", nullable = false)
    private String sellComAdr;    // 회사 주소

    @Column(name = "sell_com_num", nullable = false)
    private String sellComNum;    // 회사 전화번호

    @Column(name = "sell_com_email", nullable = false)
    private String sellComEmail;  // 회사 이메일

    // --- [담당자 정보] ---
    @Column(name = "sell_bm_name", nullable = false)
    private String sellBmName;    // 담당자명

    @Column(name = "sell_bm_num", nullable = false)
    private String sellBmNum;     // 담당자 연락처

    @Column(name = "sell_bm_dep", nullable = false)
    private String sellBmDep;     // 담당자 부서

    // --- [상태 및 구분] ---
    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType; // SELLER / BUYER

    @Column(name = "approv_yn", nullable = false, length = 1)
    @Builder.Default
    private String approvYn = "N"; // 승인 여부

    @Column(name = "marketing_check", nullable = false, length = 1)
    private String marketingCheck; // 마케팅 동의 여부

    @CreationTimestamp // ★ 핵심: INSERT 쿼리가 날아갈 때 자동으로 현재 시간을 넣어줍니다.
    @Column(name = "reg_date", updatable = false) // 정보 수정할 때 이 날짜는 바뀌면 안 되니까 false
    private LocalDateTime regDate;

    // [편의 메서드] 비밀번호 변경
    public void updatePassword(String newPassword) {
        this.sellComPw = newPassword;
    }
}