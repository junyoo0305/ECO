package com.example.seller.selpost.service;

//import com.example.seller.repository.PostFileRepository; // import

import com.example.seller.mypage.model.User;
import com.example.seller.mypage.repository.UserRepository;
import com.example.seller.selpost.dto.SalesPostResponseDto;
import com.example.seller.selpost.repository.SalesPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerPostService {

    private final SalesPostRepository salesPostRepository;
    // 문자열 ID로 실제 회원의 PK(Long)를 찾기 위해 주입
    private final UserRepository userRepository;

    // 파일 저장을 위해 필요
//    private final PostFileRepository postFileRepository;
//
//    private final String uploadPath = "C:/eco_images/";

    // 컨트롤러가 호출하는 메서드 (String ID 받음)
    @Transactional(readOnly = true)
    public List<SalesPostResponseDto> getPostsBySellComId(String sellComId) {

        // 문자열 ID("solar123")로 회원 엔티티 찾기
        User member = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + sellComId));

        // 엔티티에서 진짜 PK(Long) 꺼내기
        Long realSellerId = member.getId();

        // 기존 로직(숫자 ID로 조회) 재사용
        return getPostsBySellerId(realSellerId);
    }

    // 내 판매글 목록 조회
    @Transactional(readOnly = true)
    public List<SalesPostResponseDto> getPostsBySellerId(Long sellerId) {
        return salesPostRepository.findBySellerIdAndDelYn(sellerId, "N").stream()
                .map(SalesPostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}