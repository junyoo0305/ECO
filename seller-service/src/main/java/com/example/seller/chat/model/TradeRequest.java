package com.example.seller.chat.model;

import com.example.seller.mypage.model.CorporateMember; // 내꺼(판매자)는 있으니까 Import
// import com.example.buyer.... BuyerMember;  <-- ❌ 이건 없으니까 삭제!

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trade_requests")
public class TradeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Long id;

    // 게시글 ID
    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "POST_TYPE", nullable = false, length = 10)
    private String postType;

    // 내 정보(판매자)는 객체로 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID", nullable = false)
    private CorporateMember seller;

    @Column(name = "BUYER_ID", nullable = false)
    private Long buyerId;

    @Column(name = "INITIATOR", nullable = false, length = 10)
    private String initiator;

    @Column(name = "STATE", length = 20)
    @ColumnDefault("'OPEN'")
    @Builder.Default
    private String state = "OPEN";

    @Column(name = "DEL_YN", length = 1)
    @ColumnDefault("'N'")
    @Builder.Default
    private String delYn = "N";

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tradeRequest", cascade = CascadeType.ALL)
    @OrderBy("sentAt ASC")
    @Builder.Default
    private List<RequestMessage> messages = new ArrayList<>();

    // 소프트 딜리트
    public void delete() {
        this.delYn = "Y";
    }
}