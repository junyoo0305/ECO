package com.example.admin.controller;

import com.example.admin.dto.AdminRequestDto;
import com.example.admin.model.Admin;
import com.example.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 1. 아이디 중복 확인 API
    // 요청 URL: /api/admins/check-id?userId=입력한아이디
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkIdDuplicate(@RequestParam("userId") String userId) {
        boolean isDuplicate = adminService.checkIdDuplicate(userId);
        return ResponseEntity.ok(isDuplicate); // 중복이면 true, 아니면 false 반환
    }

    // 관리자 등록
    @PostMapping
    public ResponseEntity<Long> register(@RequestBody AdminRequestDto requestDto) {
        return ResponseEntity.ok(adminService.save(requestDto));
    }

    // 관리자 목록 조회
    @GetMapping
    public ResponseEntity<List<Admin>> getList() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{seq}")
    public ResponseEntity<Admin> getDetail(@PathVariable Long seq) {
        return ResponseEntity.ok(adminService.findById(seq));
    }

    // 수정
    @PutMapping("/{seq}")
    public ResponseEntity<Void> update(@PathVariable Long seq, @RequestBody AdminRequestDto requestDto) {
        adminService.update(seq, requestDto);
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> delete(@PathVariable Long seq) {
        adminService.delete(seq);
        return ResponseEntity.ok().build();
    }
}
