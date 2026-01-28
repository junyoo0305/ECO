package com.example.inquiry.dto;


import com.example.inquiry.model.Inquiry;
import com.example.inquiry.model.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 기본 생성자 생성 (Jackson 라이브러리가 필요로 함)
public class InquiryResponseDto {
    private Long id;
    private Long inquirerId;
    private Long recipientId;
    private PostType postType;
    private Long postId;
    private String title;
    private String content;
    private String replyContent;
    private String isAnswered;
    private LocalDateTime createdDate;
    private LocalDateTime replyDate;

    private String inquirerName;
    private String postTitle;


    public InquiryResponseDto(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.inquirerId = inquiry.getInquirerId();
        this.recipientId = inquiry.getRecipientId();
        this.postType = inquiry.getPostType();
        this.postId = inquiry.getPostId();
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        this.replyContent = inquiry.getReplyContent();
        this.isAnswered = inquiry.getIsAnswered();
        this.createdDate = inquiry.getCreatedDate();
        this.replyDate = inquiry.getReplyDate();

        this.inquirerName = inquiry.getInquirerName();
        this.postTitle = inquiry.getPostTitle();
    }
}