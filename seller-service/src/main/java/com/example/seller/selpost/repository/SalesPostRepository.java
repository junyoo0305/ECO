package com.example.seller.selpost.repository; // [수정] 패키지명 seller

import com.example.seller.selpost.model.SalesPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesPostRepository extends JpaRepository<SalesPost, Long> {
    List<SalesPost> findBySellerIdAndDelYn(Long sellerId, String delYn);
//    List<SalesPost> findByDelYn(String delYn);
}