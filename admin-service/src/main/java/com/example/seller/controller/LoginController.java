package com.example.seller.controller;

import com.example.seller.model.Admin;
import com.example.seller.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AdminService adminService;

    // 2. 로그인 처리 (API)
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String userId = params.get("userId");
        String password = params.get("password");

        Admin loginAdmin = adminService.login(userId, password);

        if (loginAdmin == null) {
            return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 맞지 않습니다.");
        }

        // [중요] 세션 생성 및 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginAdmin", loginAdmin); // 세션에 'loginAdmin'이라는 이름으로 저장

        return ResponseEntity.ok().body("로그인 성공");
    }

    // 3. 로그아웃 처리
    @PostMapping("/api/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 날리기
        }
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    // [추가] 현재 로그인 상태인지 확인하는 API
    @GetMapping("/api/check-login")
    @ResponseBody
    public ResponseEntity<?> checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        // 1. 세션이 없으면 401 에러
        if (session == null || session.getAttribute("loginAdmin") == null) {
            return ResponseEntity.status(401).body("Not Logged In");
        }

        // 2. 세션에서 로그인한 관리자 정보 꺼내기 (AdminDTO 라고 가정)
        // (import com.example.admin.dto.AdminDTO; 가 필요할 수 있습니다)
        Admin admin = (Admin) session.getAttribute("loginAdmin");

        // 3. JSON 형식으로 권한(auth) 정보를 담아서 리턴!
        // 예: {"message": "Logged In", "auth": "2"}
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logged In");
        response.put("auth", admin.getAuth()); // 권한 값 (1: 슈퍼, 2: 일반)

        return ResponseEntity.ok(response);
    }
}
