package com.example.seller.chat.service;

import com.example.seller.chat.dto.ChatMessageDto;
import com.example.seller.chat.dto.ChatRoomDto;
import com.example.seller.chat.model.RequestMessage;
import com.example.seller.chat.model.TradeRequest;
import com.example.seller.chat.repository.RequestMessageRepository;
import com.example.seller.chat.repository.TradeRequestRepository;
import com.example.seller.selpost.repository.SalesPostRepository; // 판매글 Repo Import 필요

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final TradeRequestRepository tradeRequestRepository;
    private final RequestMessageRepository messageRepository;
    private final SalesPostRepository salesPostRepository; // 글 제목 조회용



    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getMyChatRooms(Long sellerId) {
        List<TradeRequest> rooms = tradeRequestRepository.findAllBySeller_SellerIdOrderByCreatedAtDesc(sellerId);
        List<ChatRoomDto> dtos = new ArrayList<>();

        for (TradeRequest room : rooms) {
            // 구매자 이름 가져오기 (Native Query)
            String buyerName = tradeRequestRepository.findBuyerNameById(room.getBuyerId());
            if (buyerName == null) buyerName = "(알수없음)";

            // 게시글 제목 가져오기 (SalesPostRepository 사용)
            // 실제로는 postType이 'SALE'인지 확인해야 하지만 편의상 바로 조회
            String postTitle = salesPostRepository.findById(room.getPostId())
                    .map(post -> post.getTitle())
                    .orElse("(삭제된 게시글)");

            // 마지막 메시지 찾기
            String lastMsg = "";
            if (!room.getMessages().isEmpty()) {
                lastMsg = room.getMessages().get(room.getMessages().size() - 1).getContent();
            }

            // DTO 변환
            dtos.add(ChatRoomDto.builder()
                    .id(room.getId())
                    .roomName(buyerName + "님과의 대화") // 방 이름
                    .opponentName(buyerName)
                    .postTitle(postTitle)
                    .lastMessage(lastMsg)
                    .state(room.getState())
                    .build());
        }
        return dtos;
    }

    // 채팅방 상세 조회 (메시지 내역)
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getMessages(Long requestId) {
        TradeRequest room = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("방이 없습니다."));

        return room.getMessages().stream()
                .map(msg -> ChatMessageDto.builder()
                        .messageId(msg.getId())
                        .content(msg.getContent())
                        .writerType(msg.getWriterType()) // SELLER or BUYER
                        .sentAt(msg.getSentAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 채팅방 정보 1개만 상세 조회하는 메서드
    @Transactional(readOnly = true)
    public ChatRoomDto getChatRoomInfo(Long requestId) {
        TradeRequest room = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));

        // 구매자 이름 가져오기
        String buyerName = tradeRequestRepository.findBuyerNameById(room.getBuyerId());
        if (buyerName == null) buyerName = "(알수없음)";

        // 게시글 제목 가져오기
        String postTitle = salesPostRepository.findById(room.getPostId())
                .map(post -> post.getTitle())
                .orElse("(삭제된 게시글)");

        return ChatRoomDto.builder()
                .id(room.getId())
                .roomName(postTitle)     // 방 이름을 게시글 제목으로
                .opponentName(buyerName) // 상대방 이름
                .postTitle(postTitle)
                .build();
    }

    // 메시지 전송
    @Transactional
    public void sendMessage(Long requestId, String content, String writerType) {
        TradeRequest room = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("방이 없습니다."));

        RequestMessage message = RequestMessage.builder()
                .tradeRequest(room)
                .content(content)
                .writerType(writerType) // "SELLER"
                .build();

        messageRepository.save(message);
    }
}