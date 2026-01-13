package com.example.seller.mypage.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_managers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUB_MANAGER_ID")
    private Long subManagerId;

    @Column(name = "SELLER_ID", nullable = false)
    private Long sellerId;

    @Column(name = "MANAGER_NAME", nullable = false, length = 50)
    private String managerName;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "DEPARTMENT", length = 50)
    private String department;

    @Column(name = "DEL_YN", nullable = false, length = 1)
    @Builder.Default // 빌더 패턴 사용 시 기본값 적용
    private String delYn = "N"; // 기본값 'N'
}