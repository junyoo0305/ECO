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

    // 글쓰기 완료
    @PostMapping("/market/write")
    public String createPost(@ModelAttribute MarketPostRequestDto dto) { // @ModelAttribute 권장
        marketPostService.createPost(dto);
        return "redirect:/market/marketpost";
    }

    // 상세 페이지
    @GetMapping("/market/post/{id}")
    public String postDetailPage(@PathVariable Long id, Model model) {
        MarketPostResponseDto post = marketPostService.getPostById(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

    // 글 수정
    @GetMapping("/market/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        MarketPostResponseDto post = marketPostService.getPostById(id);
        model.addAttribute("post", post);
        return "post_edit";
    }

    // 수정 완료
    @PostMapping("/market/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute MarketPostRequestDto dto) {
        marketPostService.updatePost(id, dto);
        return "redirect:/market/marketpost";
    }

    // ... 나머지 삭제, 상태변경, 문의 메서드는 기존과 동일 ...
    @PostMapping("/market/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        marketPostService.deletePost(id);
        return "redirect:/seller/sellerpost";
    }

    // 상태 업데이트
    @PostMapping("/market/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        marketPostService.updateStatus(id, status);
        return "redirect:/seller/sellerpost";
    }
}