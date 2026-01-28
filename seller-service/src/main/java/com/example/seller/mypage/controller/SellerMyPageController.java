package com.example.seller.mypage.controller;

import com.example.seller.mypage.dto.CorporateMemberResponseDto;
import com.example.seller.mypage.dto.PasswordUpdateDto;
import com.example.seller.mypage.model.SubManager;
import com.example.seller.mypage.repository.UserRepository;
import com.example.seller.mypage.service.SellerMyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellerMyPageController {

    private final SellerMyPageService sellerMyPageService;
    private final UserRepository corporateMemberRepository;

    // [핵심] 권한 체크용 유틸 메서드 (코드가 중복되니 하나로 뺌)
    private void checkSellerAuthority(String role) {
        if (!"SELLER".equals(role)) {
            // 게이트웨이에서 막겠지만, 2차 방어로 예외 발생시킴
            throw new IllegalArgumentException("접근 권한이 없습니다. (판매자 전용)");
        }
    }

    @GetMapping("/seller/profile")
    public String profile(Model model,
                          @RequestHeader(value = "X-USER-ID", required = false) String sellComId,
                          @RequestHeader(value = "X-USER-ROLE", required = false) String role) {
        // 로그인 여부 확인
        if (sellComId == null) {
            return "redirect:/login";
        }

        // 판매자 권한 확인 (구매자가 URL로 직접 들어오는 것 방지)
        checkSellerAuthority(role);

        // 서비스 호출
        CorporateMemberResponseDto myInfo = sellerMyPageService.getMyInfo(sellComId);
        model.addAttribute("info", myInfo);

        // 담당자 목록 조회
        List<SubManager> managers = sellerMyPageService.getManagers(sellComId);
        model.addAttribute("managers", managers);

        return "profile";
    }

    // 담당자 추가
    @PostMapping("/seller/manager/add")
    public String addManager(@RequestHeader("X-USER-ID") String sellComId,
                             @RequestHeader("X-USER-ROLE") String role, // role 추가
                             @RequestParam("managerName") String name,
                             @RequestParam String email,
                             @RequestParam String department,
                             @RequestParam String phone) {

        checkSellerAuthority(role); // 권한 체크
        sellerMyPageService.addManager(sellComId, name, email, department, phone);

        return "redirect:/seller/profile";
    }

    // 담당자 수정
    @PostMapping("/seller/manager/update/{id}")
    public String updateManager(@RequestHeader("X-USER-ID") String sellComId,
                                @RequestHeader("X-USER-ROLE") String role, // role 추가
                                @PathVariable Long id,
                                @RequestParam String managerName,
                                @RequestParam String department,
                                @RequestParam String phone,
                                @RequestParam String email) {

        checkSellerAuthority(role); // 권한 체크
        sellerMyPageService.updateManager(sellComId, id, managerName, email, department, phone);

        return "redirect:/seller/profile";
    }

    // 담당자 삭제
    @PostMapping("/seller/manager/delete/{id}")
    public String deleteManager(@RequestHeader("X-USER-ID") String sellComId,
                                @RequestHeader("X-USER-ROLE") String role, // role 추가
                                @PathVariable Long id) {

        checkSellerAuthority(role); // 권한 체크
        sellerMyPageService.deleteManager(sellComId, id);

        return "redirect:/seller/profile";
    }

    // 비밀번호 변경 페이지
    @GetMapping("/seller/passwordUpdate")
    public String passwordUpdatePage(@RequestHeader(value = "X-USER-ROLE", required = false) String role) {
        checkSellerAuthority(role); // 페이지 진입 시에도 체크
        return "passwordUpdate";
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("/seller/updatePassword")
    public String updatePassword(@RequestHeader("X-USER-ID") String sellComId,
                                 @RequestHeader("X-USER-ROLE") String role, // role 추가
                                 PasswordUpdateDto dto,
                                 RedirectAttributes redirectAttributes) {

        checkSellerAuthority(role); // 권한 체크
        try {
            sellerMyPageService.changePassword(sellComId, dto);

            redirectAttributes.addFlashAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/seller/profile";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/seller/passwordUpdate";
        }
    }

    // 회원 탈퇴 처리
    @PostMapping("/seller/withdraw")
    public String withdraw(@RequestHeader("X-USER-ID") String sellComId,
                           @RequestHeader("X-USER-ROLE") String role) { // role 추가
        checkSellerAuthority(role); // 권한 체크
        sellerMyPageService.withdrawSeller(sellComId);
        return "redirect:/market/marketpost";
    }
}