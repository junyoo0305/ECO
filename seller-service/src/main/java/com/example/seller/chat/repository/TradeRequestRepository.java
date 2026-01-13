package com.example.seller.chat.repository;

import com.example.seller.chat.model.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    // Seller 객체 안의 sellerId 변수를 찾으라고 명시 (_SellerId)
    List<TradeRequest> findAllBySeller_SellerIdOrderByCreatedAtDesc(Long sellerId);

    // 구매자 테이블(buyer_members)에서 이름만 쏙 빼오는 쿼리
    @Query(value = "SELECT BUY_COM_NAME FROM buyer_members WHERE BUYER_ID = :buyerId", nativeQuery = true)
    String findBuyerNameById(@Param("buyerId") Long buyerId);
}