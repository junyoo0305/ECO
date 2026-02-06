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
public class BuyerManageService {

    private final UserRepository userRepository;

    // 1. 구매자 목록 조회 (BUYER만 필터링)
    @Transactional(readOnly = true)
    public List<User> getBuyerList() {
        return userRepository.findByCompanyType(CompanyType.BUYER);
    }

    // 2. 구매자 상세 조회
    @Transactional(readOnly = true)
    public User getBuyerDetail(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    // 3. 승인 상태 변경
    @Transactional
    public void updateApproval(Long id, String approvalYn) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        user.setApprovYn(approvalYn);
    }
}