package com.example.seller.mypage.repository;

import com.example.seller.mypage.model.SubManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubManagerRepository extends JpaRepository<SubManager, Long> {
    // [수정] SellerId -> UserId 로 변경
    List<SubManager> findByUserIdAndDelYn(Long userId, String delYn);
}