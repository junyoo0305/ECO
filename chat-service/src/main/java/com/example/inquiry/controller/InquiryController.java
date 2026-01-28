package com.example.inquiry.controller;

import com.example.inquiry.dto.InquiryRequestDto;
import com.example.inquiry.dto.InquiryResponseDto;
import com.example.inquiry.service.InquiryService; // 서비스 이름 확인 필요
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService serviceInquiry;

    // 문의 등록
    @PostMapping("/add")
    public ResponseEntity<String> addInquiry(
            @RequestHeader("X-USER-ID") String sellComId,
            @RequestBody InquiryRequestDto dto) {
        serviceInquiry.createInquiry(sellComId, dto);
        return ResponseEntity.ok("문의 등록 완료");
    }

    // 답변 등록
    @PostMapping("/reply/{inquiryId}")
    public ResponseEntity<String> replyInquiry(
            @RequestHeader("X-USER-ID") String sellComId,
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> requestBody) {
        serviceInquiry.replyInquiry(sellComId, inquiryId, requestBody.get("replyContent"));
        return ResponseEntity.ok("답변 등록 완료");
    }

    // 내가 보낸 문의 데이터 조회
    @GetMapping("/sent")
    public ResponseEntity<List<InquiryResponseDto>> getSentInquiries(
            @RequestHeader("X-USER-ID") String sellComId) {
        return ResponseEntity.ok(serviceInquiry.getMySentInquiries(sellComId));
    }

    // 나에게 온 문의 데이터 조회
    @GetMapping("/received")
    public ResponseEntity<List<InquiryResponseDto>> getReceivedInquiries(
            @RequestHeader("X-USER-ID") String sellComId) {
        return ResponseEntity.ok(serviceInquiry.getMyReceivedInquiries(sellComId));
    }
}