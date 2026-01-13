package com.example.seller.chat.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_messages")
public class RequestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long id;

    // 채팅방 연결 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private TradeRequest tradeRequest;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "WRITER_TYPE", nullable = false, length = 10)
    private String writerType; // "SELLER", "BUYER"

    @Column(name = "IS_READ", length = 1)
    @ColumnDefault("'N'")
    @Builder.Default
    private String isRead = "N";

    @Column(name = "DEL_YN", length = 1)
    @ColumnDefault("'N'")
    @Builder.Default
    private String delYn = "N";

    @CreationTimestamp
    @Column(name = "SENT_AT", updatable = false)
    private LocalDateTime sentAt;

    // 메시지 삭제 (Soft Delete)
    public void delete() {
        this.delYn = "Y";
    }

    // 읽음 처리
    public void markAsRead() {
        this.isRead = "Y";
    }
}