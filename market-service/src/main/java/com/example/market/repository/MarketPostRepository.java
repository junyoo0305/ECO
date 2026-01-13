package com.example.market.repository;

import com.example.market.model.MarketPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPostRepository extends JpaRepository<MarketPost, Long>, JpaSpecificationExecutor<MarketPost> {
}