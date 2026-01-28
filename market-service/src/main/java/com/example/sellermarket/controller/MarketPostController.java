package com.example.sellermarket.controller;

import com.example.sellermarket.dto.MarketPostRequestDto;
import com.example.sellermarket.dto.MarketPostResponseDto;
import com.example.sellermarket.dto.MarketPostSearchDto;
import com.example.sellermarket.dto.PublicCompanyDto;
import com.example.sellermarket.repository.SubManagerRepository;
import com.example.sellermarket.repository.UserRepository;
import com.example.sellermarket.service.MarketPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MarketPostController {

    private final MarketPostService marketPostService;
    private final UserRepository userRepository;
    private final SubManagerRepository subManagerRepository;

    // 목록 조회
    @GetMapping("/market/marketpost")
    public String market(Model model,
                         @RequestParam(value="page", defaultValue="0") int page,
                         @RequestParam(value="sort", defaultValue="latest") String sort,
                         @ModelAttribute MarketPostSearchDto searchDto,
                         @RequestHeader(value = "X-USER-ID", required = false) String sellComId){

        Page<MarketPostResponseDto> paging = marketPostService.getPaging(page, sort, searchDto);

        model.addAttribute("paging", paging);
        model.addAttribute("sort", sort);
        model.addAttribute("searchDto", searchDto);

        // 로그인한 사용자 정보 조회해서 모델에 담기
        if (sellComId != null) {
            userRepository.findBySellComId(sellComId).ifPresent(user -> {
                model.addAttribute("user", user); // user 객체 전체를 넘김
            });
        }

        return "marketpost";
    }

    @GetMapping("/market/write")
    public String writePage() {
        return "post_write";
    }

    // 상세 페이지
    @GetMapping("/market/post/{id}")
    public String postDetailPage(@PathVariable Long id,
                                 @RequestHeader(value = "X-USER-ID", required = false) String sellComId,
                                 Model model) {
        // 게시글 정보 조회
        MarketPostResponseDto post = marketPostService.getPostById(id);
        model.addAttribute("post", post);

        // 게시글 작성자(판매자)의 회사 이름 조회 및 추가
        // post.getSellerId()가 Long(PK) 타입인 경우
        if (post.getSellerId() != null) {
            userRepository.findById(post.getSellerId()).ifPresent(seller -> {
                model.addAttribute("sellerName", seller.getSellComName());
            });
        }

        // 로그인한 사용자 정보 조회 및 모델 추가
        if (sellComId != null) {
            userRepository.findBySellComId(sellComId).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }

        return "post_detail";
    }

    // 글쓰기 완료
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

    // 수정 페이지 이동
    @GetMapping("/market/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        MarketPostResponseDto post = marketPostService.getPostById(id);
        model.addAttribute("post", post);

        return "post_edit"; // post_edit.html 템플릿 반환
    }

    // 글 수정 완료
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

    // 글 삭제
    @PostMapping("/market/delete/{id}")
    public String deletePost(
            @PathVariable Long id,
            @RequestHeader(value = "X-USER-ID", required = false) String userId
    ) {
        if (userId == null) userId = "testUser";
        marketPostService.deletePost(id, userId);
        return "redirect:/seller/sellerpost";
    }

    // MarketPostController.java
    @GetMapping("/market/api/company/{id}") // 변수명을 id로 변경
    public ResponseEntity<PublicCompanyDto> getPublicCompanyInfo(@PathVariable Long id) { // String -> Long으로 변경
        // findBySellComId 대신 findById(PK조회)를 사용하도록 수정
        return userRepository.findById(id).map(user -> {

            // 추가 담당자(SubManager) 목록 조회 (삭제되지 않은 것만)
            List<PublicCompanyDto.SubManagerDto> subManagerDtos = subManagerRepository.findByUserIdAndDelYn(user.getId(), "N")
                    .stream()
                    .map(sm -> new PublicCompanyDto.SubManagerDto(
                            sm.getManagerName(),
                            sm.getPhone(),
                            sm.getEmail(),
                            sm.getDepartment()))
                    .collect(Collectors.toList());

            // 전체 데이터 합쳐서 반환
            return ResponseEntity.ok(PublicCompanyDto.builder()
                    .sellComName(user.getSellComName())
                    .sellComAdr(user.getSellComAdr())
                    .sellComEmail(user.getSellComEmail())
                    .mainManagerName(user.getSellBmName())
                    .mainManagerNum(user.getSellBmNum())
                    .mainManagerDep(user.getSellBmDep())
                    .subManagers(subManagerDtos)
                    .build());
        }).orElse(ResponseEntity.notFound().build());
    }

    // 상태 업데이트
    @PostMapping("/market/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        marketPostService.updateStatus(id, status);
        return "redirect:/seller/sellerpost";
    }
}