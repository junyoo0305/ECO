package com.example.admin.repository;

import com.example.admin.model.CompanyType;
import com.example.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // [핵심] 회사 타입(SELLER/BUYER)으로 리스트 조회하는 쿼리 자동 생성
    // SQL: SELECT * FROM users WHERE company_type = ?
    List<User> findByCompanyType(CompanyType companyType);

    long countByCompanyType(CompanyType companyType);

    // 월별 가입자 수 (regDate 필드 필수)
    @Query("SELECT FUNCTION('DATE_FORMAT', u.regDate, '%Y-%m') as date, COUNT(u) " +
            "FROM User u WHERE u.companyType = :type AND u.regDate BETWEEN :start AND :end " +
            "GROUP BY date ORDER BY date ASC")
    List<Object[]> statsByMonth(@Param("type") CompanyType type, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}