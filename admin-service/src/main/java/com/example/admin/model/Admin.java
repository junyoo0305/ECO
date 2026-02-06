package com.example.admin.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "Admin",
        indexes = {
                // 1. PK 인덱스 명시 (이미지에 나온 이름 적용)
                @Index(name = "Admin_IDX_PK", columnList = "SEQ_NO_Admin"),

                // 2. 보조 복합 인덱스 (이미지에 나온 이름 적용)
                @Index(name = "Admin_IDX_1", columnList = "SEQ_NO_Admin, DEL_YN")
        }
)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DDL: IDENTITY(1,1)
    @Column(name = "SEQ_NO_Admin") // PK 컬럼명
    private Long seq;

    @Column(name = "ID", nullable = false, length = 100)
    private String userId;

    @Column(name = "PW", nullable = false, length = 200)
    private String password;

    @Column(name = "NM", nullable = false, length = 100)
    private String name;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "DEPARTMENT", length = 500) // NULL 허용
    private String department;

    @Column(name = "PHONE", length = 100) // NULL 허용
    private String phone;

    @Column(name = "AUTH", nullable = false, length = 1) // CHAR(1)
    private String auth; // 1 : 슈퍼 관리자 2 : 일반 관리자

    @CreatedDate // INSERT 시 현재 날짜 자동 주입
    @Column(name = "INSERT_DATE", nullable = false, updatable = false)
    private LocalDate insertDate; // DDL: DATE 타입 -> Java: LocalDate

    @Column(name = "DEL_YN", nullable = false, length = 1)
    private String delYn = "N"; // DDL: NOT NULL -> 기본값 'N' 지정 필수

    @Builder
    public Admin(String userId, String password, String name, String email,
                       String department, String phone, String auth) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.auth = auth;
        // seq, insertDate는 자동 생성
        // delYn은 위에서 "N"으로 초기화됨
    }
}
