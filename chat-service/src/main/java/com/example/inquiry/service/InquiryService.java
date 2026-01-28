package com.example.inquiry.service;


import com.example.inquiry.dto.InquiryRequestDto;
import com.example.inquiry.dto.InquiryResponseDto;
import com.example.inquiry.model.Inquiry;
import com.example.inquiry.model.User;
import com.example.inquiry.repository.InquiryRepository;
import com.example.inquiry.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository; // ID 변환용

    // 1. 유저 ID 변환 (String sellComId -> Long PK)
    private Long getUserPk(String sellComId) {
        // [수정됨] findById가 아니라 findBySellComId를 써야 합니다!
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return user.getId();
    }

    // 2. 문의 등록
    @Transactional
    public void createInquiry(String sellComId, InquiryRequestDto dto) {
        Long inquirerPk = getUserPk(sellComId); // 질문자 PK 찾기

        Inquiry inquiry = new Inquiry();
        inquiry.setInquirerId(inquirerPk);
        inquiry.setRecipientId(dto.getRecipientId()); // 프론트에서 받은 수신자 PK
        inquiry.setPostType(dto.getPostType());       // SALES or PURCHASE
        inquiry.setPostId(dto.getPostId());
        inquiry.setTitle(dto.getTitle());
        inquiry.setContent(dto.getContent());
        inquiry.setIsSecret(dto.isSecret() ? "Y" : "N");

        // 회사명, 타이틀 추가
        inquiry.setInquirerName(dto.getInquirerName());
        inquiry.setPostTitle(dto.getPostTitle());

        inquiryRepository.save(inquiry);
    }

    // 3. 답변 등록
    @Transactional
    public void replyInquiry(String sellComId, Long inquiryId, String replyContent) {
        Long userPk = getUserPk(sellComId);

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 없습니다."));

        // 본인 확인 (답변하려는 사람이 수신자가 맞는지?)
        if (!inquiry.getRecipientId().equals(userPk)) {
            throw new IllegalArgumentException("답변 권한이 없습니다.");
        }

        inquiry.registerReply(replyContent);
    }

    // 4. 내가 보낸 문의 조회
    @Transactional(readOnly = true)
    public List<InquiryResponseDto> getMySentInquiries(String sellComId) {
        Long userPk = getUserPk(sellComId);
        return inquiryRepository.findByInquirerIdOrderByCreatedDateDesc(userPk)
                .stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }

    // 5. 나에게 온 문의 조회
    @Transactional(readOnly = true)
    public List<InquiryResponseDto> getMyReceivedInquiries(String sellComId) {
        Long userPk = getUserPk(sellComId);
        return inquiryRepository.findByRecipientIdOrderByCreatedDateDesc(userPk)
                .stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }
}