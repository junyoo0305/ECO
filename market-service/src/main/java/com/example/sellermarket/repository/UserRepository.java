package com.example.sellermarket.repository;

import com.example.sellermarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySellComId(String sellComId);
}