package com.example.market.controller;

import com.example.market.dto.MarketPostRequestDto;
import com.example.market.dto.MarketPostResponseDto;
import com.example.market.dto.MarketPostSearchDto;
import com.example.market.service.MarketPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException; // 필요 시 import

@Controller
@RequiredArgsConstructor
public class MarketPostController {

    private final MarketPostService marketPostService;

    // 목록 조회
    @GetMapping("/market/marketpost")
    public String market(Model model,
                         @RequestParam(value="page", defaultValue="0") int page,
                         @RequestParam(value="sort", defaultValue="latest") String sort,
                         @ModelAttribute MarketPostSearchDto searchDto){

        Page<MarketPostResponseDto> paging = marketPostService.getPaging(page, sort, searchDto);

        model.addAttribute("paging", paging);
        model.addAttribute("sort", sort);
        model.addAttribute("searchDto", searchDto);

        return "marketpost";
    }

    @GetMapping("/market/write")
    public String writePage() {
        return "post_write";
    }

    // 상세 페이지
    @GetMapping("/market/post/{id}")
    public String postDetailPage(@PathVariable Long id, Model model) {
        MarketPostResponseDto post = marketPostService.getPostById(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

    // [수정] 글쓰기 완료
    @PostMapping("/market/write")
    public String createPost(
            // [변경] 게이트웨이가 준 ID는 문자열(String)입니다! (Long -> String)
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @ModelAttribute MarketPostRequestDto dto
    ) {
        // 테스트용: 헤더 없으면 임시 ID 사용
        if (userId == null) userId = "testUser"; // 테스트용 문자열 ID

        // 서비스로 문자열 ID를 넘김
        marketPostService.createPost(dto, userId);
        return "redirect:/market/marketpost";
    }

    // [수정] 글 수정 완료
    @PostMapping("/market/edit/{id}")
    public String updatePost(
            @PathVariable Long id,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @ModelAttribute MarketPostRequestDto dto
    ) {
        if (userId == null) userId = "testUser";
        marketPostService.updatePost(id, userId, dto);
        return "redirect:/market/marketpost";
    }

    // [수정] 글 삭제
    @PostMapping("/market/delete/{id}")
    public String deletePost(
            @PathVariable Long id,
            @RequestHeader(value = "X-USER-ID", required = false) String userId
    ) {
        if (userId == null) userId = "testUser";
        marketPostService.deletePost(id, userId);
        return "redirect:/seller/sellerpost";
    }

    // 상태 업데이트
    @PostMapping("/market/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        marketPostService.updateStatus(id, status);
        return "redirect:/seller/sellerpost";
    }
}