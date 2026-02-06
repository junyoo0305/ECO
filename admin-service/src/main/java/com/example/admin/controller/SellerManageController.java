package com.example.admin.controller;

import com.example.admin.model.User;
import com.example.admin.service.SellerManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/sellers") // HTML의 fetch 주소와 일치
@RequiredArgsConstructor
public class SellerManageController {

    private final SellerManageService sellerManageService;

    // 1. 판매자 목록 조회
    // GET /api/sellers
    @GetMapping
    public ResponseEntity<List<User>> getList() {
        List<User> sellers = sellerManageService.getSellerList();
        return ResponseEntity.ok(sellers);
    }

    // 2. 판매자 상세 조회
    // GET /api/sellers/{seq}
    @GetMapping("/{seq}")
    public ResponseEntity<User> getDetail(@PathVariable Long seq) {
        User seller = sellerManageService.getSellerDetail(seq);
        return ResponseEntity.ok(seller);
    }

    // 3. 승인 여부 변경 (승인 처리)
    // PUT /api/sellers/{seq}/approval
    @PutMapping("/{seq}/approval")
    public ResponseEntity<Void> approve(@PathVariable Long seq, @RequestBody Map<String, String> body) {
        String approvalYn = body.get("approvalYn"); // HTML에서 보낸 { approvalYn: 'Y' } 받기
        sellerManageService.updateApproval(seq, approvalYn);
        return ResponseEntity.ok().build();
    }
}