package com.example.admin.controller;

import com.example.admin.model.User;
import com.example.admin.service.BuyerManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/buyers")
@RequiredArgsConstructor
public class BuyerManageController {

    private final BuyerManageService buyerManageService;

    // 목록 조회
    @GetMapping
    public ResponseEntity<List<User>> getList() {
        return ResponseEntity.ok(buyerManageService.getBuyerList());
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(buyerManageService.getBuyerDetail(id));
    }

    // 승인 처리
    @PutMapping("/{id}/approval")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        buyerManageService.updateApproval(id, body.get("approvalYn"));
        return ResponseEntity.ok().build();
    }
}