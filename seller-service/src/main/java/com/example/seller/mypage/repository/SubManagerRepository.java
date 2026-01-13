package com.example.seller.mypage.repository;

import com.example.seller.mypage.model.SubManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubManagerRepository extends JpaRepository<SubManager, Long> {
    // 특정 판매자의 담당자 중 삭제되지 않은('N') 사람만 조회
    List<SubManager> findBySellerIdAndDelYn(Long sellerId, String delYn);
}
