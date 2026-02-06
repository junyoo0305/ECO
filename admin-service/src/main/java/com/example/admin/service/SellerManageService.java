package com.example.admin.service;

import com.example.admin.model.CompanyType;
import com.example.admin.model.User;
import com.example.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerManageService {

    private final UserRepository userRepository;

    // 1. 판매자 목록 조회 (SELLER만 필터링)
    @Transactional(readOnly = true)
    public List<User> getSellerList() {
        return userRepository.findByCompanyType(CompanyType.SELLER);
    }

    // 2. 판매자 상세 조회
    @Transactional(readOnly = true)
    public User getSellerDetail(Long seq) {
        return userRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    // 3. 승인 상태 변경 (Y / N)
    @Transactional
    public void updateApproval(Long seq, String approvalYn) {
        User user = userRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // Entity의 Setter나 편의 메소드 사용
        user.setApprovYn(approvalYn);
    }
}