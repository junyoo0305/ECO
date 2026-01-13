package com.example.seller.chat.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long messageId;
    private String content;       // 메세지 내용
    private String writerType;    // "SELLER" (나), "BUYER" (상대)
    private LocalDateTime sentAt; // 보낸 시간
}