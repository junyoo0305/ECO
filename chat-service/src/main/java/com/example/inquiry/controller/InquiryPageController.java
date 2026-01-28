package com.example.inquiry.controller;

import com.example.inquiry.dto.InquiryResponseDto;
import com.example.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/inquiry/view") // ★★★ 주소를 분리했습니다!
@RequiredArgsConstructor
public class InquiryPageController {

    private final InquiryService serviceInquiry;

    // 1. 받은 문의함 페이지 (화면)
    // 접속 주소: /inquiry/view/received
    @GetMapping("/received")
    public String receivedInquiriesPage(Model model,
                                        @RequestHeader("X-USER-ID") String sellComId) {
        List<InquiryResponseDto> inquiries = serviceInquiry.getMyReceivedInquiries(sellComId);
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("pageType", "received");
        return "inquiry_list"; // inquiry_list.html
    }

    // 2. 보낸 문의함 페이지 (화면)
    // 접속 주소: /inquiry/view/sent
    @GetMapping("/sent")
    public String sentInquiriesPage(Model model,
                                    @RequestHeader("X-USER-ID") String sellComId) {
        List<InquiryResponseDto> inquiries = serviceInquiry.getMySentInquiries(sellComId);
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("pageType", "sent");
        return "inquiry_list"; // inquiry_list.html
    }
}