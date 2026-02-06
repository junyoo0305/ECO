package com.example.admin.repository;

import com.example.admin.model.CompanyType;
import com.example.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // [핵심] 회사 타입(SELLER/BUYER)으로 리스트 조회하는 쿼리 자동 생성
    // SQL: SELECT * FROM users WHERE company_type = ?
    List<User> findByCompanyType(CompanyType companyType);
}