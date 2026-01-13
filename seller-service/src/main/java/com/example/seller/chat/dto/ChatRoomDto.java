package com.example.seller.chat.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String roomName;
    private String opponentName;
    private String postTitle;

    private String lastMessage;
    private LocalDateTime lastDate;
    private int unreadCount;

    private String state;
}