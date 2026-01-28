package com.example.sellermarket.repository;

import com.example.sellermarket.model.SubManager;
import com.example.sellermarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubManagerRepository extends JpaRepository<SubManager, Long> {
    List<SubManager> findByUserIdAndDelYn(Long userId, String delYn);
}