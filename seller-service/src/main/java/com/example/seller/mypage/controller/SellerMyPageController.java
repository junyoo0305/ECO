package com.example.seller.mypage.controller;

import com.example.seller.mypage.dto.CorporateMemberResponseDto;
import com.example.seller.mypage.dto.PasswordUpdateDto;
import com.example.seller.mypage.model.CorporateMember;
import com.example.seller.mypage.model.SubManager;
import com.example.seller.mypage.service.SellerMyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellerMyPageController {

    private final SellerMyPageService sellerMyPageService;

    // 1. 프로필 페이지 (URL도 profile로 맞춤)
    @GetMapping("/seller/profile")
    public String profile(Model model) {
        Long fixedSellerId = 1L; // 로그인 기능 추가시 교체

        // 서비스에서 정보 가져와서 화면에 전달
        CorporateMemberResponseDto myInfo = sellerMyPageService.getMyInfo(fixedSellerId);
        model.addAttribute("info", myInfo);

        // 담당자 목록 조회
        List<SubManager> managers = sellerMyPageService.getManagers(fixedSellerId);
        model.addAttribute("managers", managers);

        return "profile"; // [수정] templates/profile.html 을 열어라!
    }

    // 담당자 추가
    @PostMapping("/seller/manager/add")
    public String addManager(@RequestParam("managerName") String name,
                             @RequestParam String email,
                             @RequestParam String department,
                             @RequestParam String phone) {
        Long fixedSellerId = 1L; // 로그인 기능 추가시 교체
        sellerMyPageService.addManager(fixedSellerId, name, email, department, phone);

        return "redirect:/seller/profile"; // 완료 후 프로필 페이지로 복귀
    }

    // 담당자 수정
    @PostMapping("/seller/manager/update/{id}")
    public String updateManager(@PathVariable Long id,
                                @RequestParam String managerName,
                                @RequestParam String department,
                                @RequestParam String phone,
                                @RequestParam String email) {
        sellerMyPageService.updateManager(id, managerName, email, department, phone);

        return "redirect:/seller/profile"; // 완료 후 다시 프로필 페이지로 복귀
    }

    // 담당자 삭제
    @PostMapping("/seller/manager/delete/{id}")
    public String deleteManager(@PathVariable Long id) {
        sellerMyPageService.deleteManager(id);

        return "redirect:/seller/profile"; // 완료 후 프로필 페이지로 복귀
    }

    // 비밀번호 변경 페이지
    @GetMapping("/seller/passwordUpdate")
    public String passwordUpdatePage() {
        return "passwordUpdate"; // templates/passwordUpdate.html
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("/seller/updatePassword") // 1. HTML의 action 주소와 일치시킴
    public String updatePassword(PasswordUpdateDto dto, RedirectAttributes redirectAttributes) {
        Long mockSellerId = 1L;

        try {
            sellerMyPageService.changePassword(mockSellerId, dto);

            // 성공 시: 메시지 담고 '프로필'로 이동
            redirectAttributes.addFlashAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/seller/profile";

        } catch (IllegalArgumentException e) {

            // 실패 시: 에러 메시지 담고 '비밀번호 변경 페이지'로 복귀
            // (errorMsg)
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/seller/passwordUpdate";
        }
    }

//    // 회원 탈퇴 처리 (세션)
//    @PostMapping("/withdraw")
//    public String withdraw(HttpSession session) {
//        // 1. 현재 로그인된 사용자 정보 가져오기
//        CorporateMember loginMember = (CorporateMember) session.getAttribute("loginMember");
//
//        if (loginMember != null) {
//            // 2. 서비스 호출 (DB에서 삭제)
//            sellerMyPageService.withdrawSeller(loginMember.getId());
//
//            // 3. 세션 종료 (로그아웃)
//            session.invalidate();
//        }
//
//        // 4. 메인 페이지로 이동
//        return "redirect:/";
//    }

    // 회원 탈퇴 처리 (테스트용: ID 1번 고정)
    @PostMapping("/seller/withdraw")
    public String withdraw() {
        // [테스트 모드] 로그인 기능 구현 전이므로 1번 회원으로 고정
        Long fixedSellerId = 1L;

        // 서비스 호출 (DB에서 삭제)
        sellerMyPageService.withdrawSeller(fixedSellerId);

        // 로그아웃(session.invalidate)은 세션이 없으므로 생략

        // 메인 페이지로 이동
        return "redirect:/market/marketpost";
    }
}