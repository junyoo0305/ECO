package com.example.seller.selpost.service;

//import com.example.seller.repository.PostFileRepository; // import

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

    // [추가] 파일 저장을 위해 필요
//    private final PostFileRepository postFileRepository;
//
//    private final String uploadPath = "C:/eco_images/";

    // 1. 내 판매글 목록 조회
    @Transactional(readOnly = true)
    public List<SalesPostResponseDto> getPostsBySellerId(Long sellerId) {
        return salesPostRepository.findBySellerIdAndDelYn(sellerId, "N").stream()
                .map(SalesPostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}