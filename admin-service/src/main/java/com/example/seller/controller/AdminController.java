package com.example.seller.controller;

import com.example.seller.dto.AdminRequestDto;
import com.example.seller.model.Admin;
import com.example.seller.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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
