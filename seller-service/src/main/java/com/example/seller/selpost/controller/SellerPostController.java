package com.example.seller.selpost.controller;

import com.example.seller.selpost.dto.SalesPostResponseDto;
import com.example.seller.selpost.service.SellerPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerPostController {

    private final SellerPostService sellerPostService;

    // 판매자 센터 대문
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // 내 판매글 관리 목록 조회
    @GetMapping("/sellerpost")
    public String sellerPostPage(Model model) {
        // 👇 게이트웨이가 검증해서 헤더에 넣어준 ID를 바로 꺼내 씀
//        @RequestHeader("X-Seller-Id") Long sellerId,
//        Model model) {
        Long mockSellerId = 1L; // 로그인 기능 추가시 교체
        List<SalesPostResponseDto> posts = sellerPostService.getPostsBySellerId(mockSellerId);

        model.addAttribute("posts", posts);
        return "sellerpost";
        // 주의: html에서 '수정' 버튼의 링크는 마켓 서버(예: /market/edit/{id})로 걸어야 합니다.
    }

}