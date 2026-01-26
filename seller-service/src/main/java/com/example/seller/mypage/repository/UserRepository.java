package com.example.seller.mypage.repository;

import com.example.seller.mypage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySellComId(String sellComId);
}