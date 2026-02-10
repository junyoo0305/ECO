package com.example.admin.repository;

import com.example.admin.model.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketPostRepository extends JpaRepository<MarketPost, Long> {

    // 지역별 게시글 수 조회
    @Query("SELECT m.location, COUNT(m) FROM MarketPost m GROUP BY m.location")
    List<Object[]> countByLocation();

    // 월별 게시글 수 조회 (YYYY-MM 형식으로 그룹화)
    @Query("SELECT FUNCTION('DATE_FORMAT', m.regDate, '%Y-%m') as date, COUNT(m) " +
            "FROM MarketPost m GROUP BY FUNCTION('DATE_FORMAT', m.regDate, '%Y-%m') " +
            "ORDER BY date ASC")
    List<Object[]> countByMonth();

    // 기존 파일에 추가
    @Query("SELECT m.location, COUNT(m) FROM MarketPost m WHERE m.regDate BETWEEN :startDate AND :endDate GROUP BY m.location")
    List<Object[]> countByLocation(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE_FORMAT', m.regDate, '%Y-%m') as date, COUNT(m) FROM MarketPost m WHERE m.regDate BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date ASC")
    List<Object[]> countByMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 1. 월별 통계 (전체 수, 판매완료 수)
    @Query("SELECT FUNCTION('DATE_FORMAT', m.regDate, '%Y-%m') as date, COUNT(m), " +
            "SUM(CASE WHEN m.saleYn = 'N' THEN 1 ELSE 0 END) " +
            "FROM MarketPost m WHERE m.regDate BETWEEN :start AND :end " +
            "GROUP BY date ORDER BY date ASC")
    List<Object[]> statsByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 2. 지역별 통계
    @Query("SELECT m.location, COUNT(m), SUM(CASE WHEN m.saleYn = 'N' THEN 1 ELSE 0 END) " +
            "FROM MarketPost m WHERE m.regDate BETWEEN :start AND :end " +
            "GROUP BY m.location")
    List<Object[]> statsByLocation(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 3. 발전원별 통계
    @Query("SELECT m.energyType, COUNT(m), SUM(CASE WHEN m.saleYn = 'N' THEN 1 ELSE 0 END) " +
            "FROM MarketPost m WHERE m.regDate BETWEEN :start AND :end " +
            "GROUP BY m.energyType")
    List<Object[]> statsByEnergy(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 4. 계약유형별 통계
    @Query("SELECT m.contractType, COUNT(m), SUM(CASE WHEN m.saleYn = 'N' THEN 1 ELSE 0 END) " +
            "FROM MarketPost m WHERE m.regDate BETWEEN :start AND :end " +
            "GROUP BY m.contractType")
    List<Object[]> statsByContract(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}