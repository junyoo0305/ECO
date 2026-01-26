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

    @GetMapping("/seller/profile")
    public String profile(Model model, @RequestHeader(value = "X-USER-ID", required = false) String sellComId) {

        // 1. 게이트웨이를 거치지 않고 직접 접속했을 경우를 대비해 예외 처리 (선택사항)
        if (sellComId == null) {
            return "redirect:/login"; // 혹은 에러 페이지
        }

        // 2. 서비스 호출 (이제 'user123' 같은 문자열 아이디를 넘깁니다)
        // 서비스 내부에서 이 ID로 DB를 조회해서 정보를 가져오게 됩니다.
        CorporateMemberResponseDto myInfo = sellerMyPageService.getMyInfo(sellComId);
        model.addAttribute("info", myInfo);

        // 3. 담당자 목록 조회
        List<SubManager> managers = sellerMyPageService.getManagers(sellComId);
        model.addAttribute("managers", managers);

        return "profile";
    }

    // 담당자 추가
    @PostMapping("/seller/manager/add")
    public String addManager(@RequestHeader("X-USER-ID") String sellComId,
                             @RequestParam("managerName") String name,
                             @RequestParam String email,
                             @RequestParam String department,
                             @RequestParam String phone) {

        // 기존: Long fixedSellerId = 1L;
        // 변경: 헤더에서 받은 String ID를 서비스로 전달
        sellerMyPageService.addManager(sellComId, name, email, department, phone);

        return "redirect:/seller/profile";
    }

    // 담당자 수정
    @PostMapping("/seller/manager/update/{id}")
    public String updateManager(@RequestHeader("X-USER-ID") String sellComId,
                                @PathVariable Long id,
                                @RequestParam String managerName,
                                @RequestParam String department,
                                @RequestParam String phone,
                                @RequestParam String email) {

        // (권장) 판매자 ID도 같이 넘겨서, 이 담당자가 내 담당자가 맞는지 검증하면 더 안전합니다.
        sellerMyPageService.updateManager(sellComId, id, managerName, email, department, phone);

        return "redirect:/seller/profile";
    }

    // 담당자 삭제
    @PostMapping("/seller/manager/delete/{id}")
    public String deleteManager(@RequestHeader("X-USER-ID") String sellComId,
                                @PathVariable Long id) {

        // (권장) 역시 판매자 ID를 넘겨 소유권 확인 후 삭제
        sellerMyPageService.deleteManager(sellComId, id);

        return "redirect:/seller/profile";
    }

    // 비밀번호 변경 페이지
    @GetMapping("/seller/passwordUpdate")
    public String passwordUpdatePage() {
        return "passwordUpdate";
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("/seller/updatePassword")
    public String updatePassword(@RequestHeader("X-USER-ID") String sellComId,
                                 PasswordUpdateDto dto,
                                 RedirectAttributes redirectAttributes) {
        try {
            // 기존: Long mockSellerId = 1L;
            // 변경: sellComId 전달
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
    public String withdraw(@RequestHeader("X-USER-ID") String sellComId) {

        // 기존: Long fixedSellerId = 1L;
        // 변경: sellComId 전달
        sellerMyPageService.withdrawSeller(sellComId);

        // 로그아웃 처리는 클라이언트(브라우저)에서 토큰을 삭제해야 하므로
        // 여기서는 리다이렉트만 하고 프론트에서 처리하거나,
        // 필요 시 쿠키 삭제 로직 등을 추가할 수 있습니다.

        return "redirect:/market/marketpost";
    }
}