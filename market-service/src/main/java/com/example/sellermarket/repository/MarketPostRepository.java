package com.example.sellermarket.repository;

import com.example.sellermarket.model.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPostRepository extends JpaRepository<MarketPost, Long>, JpaSpecificationExecutor<MarketPost> {
}