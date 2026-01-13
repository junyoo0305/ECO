package com.example.seller.chat.controller;

import com.example.seller.chat.dto.ChatRoomDto;
import com.example.seller.chat.service.ChatService;
import com.example.seller.mypage.model.CorporateMember; // 세션 정보용
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seller/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

//    // 1. 채팅방 목록 페이지
//    @GetMapping("/list")
//    public String chatList(HttpSession session, Model model) {
//        // 세션에서 로그인한 판매자 정보 가져오기 (로그인 로직에 따라 다를 수 있음)
//        CorporateMember seller = (CorporateMember) session.getAttribute("loginMember");
//        if (seller == null) return "redirect:/seller/login"; // 로그인 안했으면 튕기기
//
//        List<ChatRoomDto> chatRooms = chatService.getMyChatRooms(seller.getId());
//        model.addAttribute("chatRooms", chatRooms);
//
//        return "seller/chat/chat_list"; // 템플릿 경로: templates/seller/chat/chat_list.html
//    }

    // 채팅방 목록 페이지 (임시)
    @GetMapping("/list")
    public String chatList(Model model) {
        // [테스트 모드] 세션 대신 1번 판매자로 고정
        // CorporateMember seller = (CorporateMember) session.getAttribute("loginMember");

        Long fixedSellerId = 1L; // ★ 여기에 테스트할 판매자 ID 입력 (DB에 있는 ID여야 함)

        // 1번 판매자의 채팅방 목록 가져오기
        model.addAttribute("chatRooms", chatService.getMyChatRooms(fixedSellerId));

        return "chat_list";
    }

    // 채팅방 상세 (대화창) 페이지
    @GetMapping("/room/{id}")
    public String chatRoom(@PathVariable Long id, Model model) {
        // 대화 내용 가져오기
        model.addAttribute("messages", chatService.getMessages(id));
        // 채팅방 정보(제목, 이름) 가져오기
        model.addAttribute("roomInfo", chatService.getChatRoomInfo(id));

        model.addAttribute("roomId", id);
        return "chat_room";
    }

    // 메시지 전송 (Form Submit or AJAX)
    @PostMapping("/send")
    public String sendMessage(@RequestParam Long requestId, @RequestParam String content) {
        // 판매자가 보내는 것이므로 writerType은 무조건 "SELLER"
        chatService.sendMessage(requestId, content, "SELLER");

        return "redirect:/seller/chat/room/" + requestId; // 전송 후 다시 방으로 리다이렉트
    }
}