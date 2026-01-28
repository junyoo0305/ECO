package com.example.inquiry.repository;

import com.example.inquiry.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 이 레포지토리는 오직 ID 변환용으로만 씁니다.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySellComId(String sellComId);
}
