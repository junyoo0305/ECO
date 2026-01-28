package com.example.inquiry.dto;

import com.example.inquiry.model.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 생성 (Jackson 라이브러리가 필요로 함)
public class InquiryRequestDto {
    private Long recipientId;   // 받는 사람의 PK (users.id)
    private PostType postType;  // "SALES" or "PURCHASE"
    private Long postId;        // 글 번호
    private String title;
    private String content;
    private boolean secret;     // 비밀글 여부
    private String inquirerName; // 추가
    private String postTitle;    // 추가
}