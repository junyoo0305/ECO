package com.example.admin.repository;

import com.example.admin.model.BuyerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BuyerPostRepository extends JpaRepository<BuyerPost, Long> {

    // 지역별 게시글 수 조회
    @Query("SELECT b.location, COUNT(b) FROM BuyerPost b GROUP BY b.location")
    List<Object[]> countByLocation();

    // 월별 게시글 수 조회
    @Query("SELECT FUNCTION('DATE_FORMAT', b.regDate, '%Y-%m') as date, COUNT(b) " +
            "FROM BuyerPost b GROUP BY FUNCTION('DATE_FORMAT', b.regDate, '%Y-%m') " +
            "ORDER BY date ASC")
    List<Object[]> countByMonth();

    // 기존 파일에 추가
    @Query("SELECT b.location, COUNT(b) FROM BuyerPost b WHERE b.regDate BETWEEN :startDate AND :endDate GROUP BY b.location")
    List<Object[]> countByLocation(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE_FORMAT', b.regDate, '%Y-%m') as date, COUNT(b) FROM BuyerPost b WHERE b.regDate BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date ASC")
    List<Object[]> countByMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE_FORMAT', b.regDate, '%Y-%m') as date, COUNT(b), " +
            "SUM(CASE WHEN b.purchaseYN = 'N' THEN 1 ELSE 0 END) " +
            "FROM BuyerPost b WHERE b.regDate BETWEEN :start AND :end " +
            "GROUP BY date ORDER BY date ASC")
    List<Object[]> statsByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b.location, COUNT(b), SUM(CASE WHEN b.purchaseYN = 'N' THEN 1 ELSE 0 END) " +
            "FROM BuyerPost b WHERE b.regDate BETWEEN :start AND :end " +
            "GROUP BY b.location")
    List<Object[]> statsByLocation(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b.energyType, COUNT(b), SUM(CASE WHEN b.purchaseYN = 'N' THEN 1 ELSE 0 END) " +
            "FROM BuyerPost b WHERE b.regDate BETWEEN :start AND :end " +
            "GROUP BY b.energyType")
    List<Object[]> statsByEnergy(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b.contractType, COUNT(b), SUM(CASE WHEN b.purchaseYN = 'N' THEN 1 ELSE 0 END) " +
            "FROM BuyerPost b WHERE b.regDate BETWEEN :start AND :end " +
            "GROUP BY b.contractType")
    List<Object[]> statsByContract(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}