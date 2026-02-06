package com.example.admin.repository;

import com.example.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUserId(String userId);

    // 아이디 존재 여부 확인
    boolean existsByUserId(String userId);
}
