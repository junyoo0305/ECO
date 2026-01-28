package com.example.sellermarket.model;

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

    // [수정] SELLER_ID -> USER_ID (통합 테이블 참조)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "MANAGER_NAME", nullable = false, length = 50)
    private String managerName;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "DEPARTMENT", length = 50)
    private String department;

    @Column(name = "DEL_YN", nullable = false, length = 1)
    @Builder.Default
    private String delYn = "N";
}