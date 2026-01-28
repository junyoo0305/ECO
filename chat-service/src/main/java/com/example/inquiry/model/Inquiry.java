package com.example.inquiry.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "inquiry")
@SQLDelete(sql = "UPDATE inquiry SET del_yn = 'Y' WHERE id = ?")
@Where(clause = "del_yn = 'N'")
public class Inquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inquirer_id", nullable = false)
    private Long inquirerId; // 질문자 PK

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId; // 수신자 PK

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false, length = 20)
    private PostType postType;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "reply_content", columnDefinition = "TEXT")
    private String replyContent;

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    @Column(name = "is_answered", length = 1)
    private String isAnswered = "N";

    @Column(name = "is_secret", length = 1)
    private String isSecret = "N";

    @Column(name = "del_yn", length = 1)
    private String delYn = "N";

    @Column(name = "inquirer_name")
    private String inquirerName; // 질문자 회사명

    @Column(name = "post_title")
    private String postTitle;    // 게시글 제목

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public void registerReply(String replyContent) {
        this.replyContent = replyContent;
        this.replyDate = LocalDateTime.now();
        this.isAnswered = "Y";
    }
}