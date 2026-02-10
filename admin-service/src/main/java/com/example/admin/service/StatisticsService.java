package com.example.admin.service;

import com.example.admin.dto.StatsDto;
import com.example.admin.model.CompanyType;
import com.example.admin.repository.BuyerPostRepository;
import com.example.admin.repository.MarketPostRepository;
import com.example.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final MarketPostRepository marketRepo;
    private final BuyerPostRepository buyerRepo;
    private final UserRepository userRepo;

    // 1. 전체 현황 (왼쪽 상단용)
    public Map<String, Long> getGlobalCounts() {
        Map<String, Long> map = new HashMap<>();
        map.put("totalUser", userRepo.count());
        map.put("totalPost", marketRepo.count() + buyerRepo.count());
        return map;
    }

    // 2. 탭별 데이터 조회
    public List<StatsDto> getStatsList(String type, String startStr, String endStr) {
        LocalDateTime start = LocalDate.parse(startStr).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endStr).atTime(23, 59, 59);

        List<StatsDto> result = new ArrayList<>();

        switch (type) {
            case "seller-month": // 판매회원(월별)
                return mapToDto(userRepo.statsByMonth(CompanyType.SELLER, start, end));
            case "buyer-month": // 구매회원(월별)
                return mapToDto(userRepo.statsByMonth(CompanyType.BUYER, start, end));

            case "match-month": // 매칭공고(월별) -> 판매+구매 합치기
                return mergeStats(marketRepo.statsByMonth(start, end), buyerRepo.statsByMonth(start, end));

            case "contract": // 계약유형별 -> 판매+구매 합치기
                return mergeStats(marketRepo.statsByContract(start, end), buyerRepo.statsByContract(start, end));

            case "location": // 지역별 -> 판매+구매 합치기
                return mergeStats(marketRepo.statsByLocation(start, end), buyerRepo.statsByLocation(start, end));

            case "energy": // 발전원별 -> 판매+구매 합치기
                return mergeStats(marketRepo.statsByEnergy(start, end), buyerRepo.statsByEnergy(start, end));
        }
        return result;
    }

    // [헬퍼] DB 결과(Object[])를 DTO로 변환 (회원 통계용)
    private List<StatsDto> mapToDto(List<Object[]> list) {
        List<StatsDto> dtos = new ArrayList<>();
        for (Object[] obj : list) {
            String key = obj[0] == null ? "미지정" : obj[0].toString();
            Long total = ((Number) obj[1]).longValue();
            dtos.add(new StatsDto(key, total, 0L)); // 회원은 거래완료 개념 없음
        }
        return dtos;
    }

    // [헬퍼] 판매글 + 구매글 데이터 합치기 (Key 기준 병합)
    private List<StatsDto> mergeStats(List<Object[]> marketList, List<Object[]> buyerList) {
        Map<String, StatsDto> map = new HashMap<>();

        // 1. 판매글 데이터 넣기
        for (Object[] obj : marketList) {
            String key = obj[0] == null ? "기타" : obj[0].toString();
            Long total = ((Number) obj[1]).longValue();
            Long done = ((Number) obj[2]).longValue(); // saleYn='N'
            map.put(key, new StatsDto(key, total, done));
        }

        // 2. 구매글 데이터 합치기
        for (Object[] obj : buyerList) {
            String key = obj[0] == null ? "기타" : obj[0].toString();
            Long total = ((Number) obj[1]).longValue();
            Long done = ((Number) obj[2]).longValue(); // purchaseYN='N'

            if (map.containsKey(key)) {
                StatsDto existing = map.get(key);
                existing.setTotalCount(existing.getTotalCount() + total);
                existing.setDoneCount(existing.getDoneCount() + done);
            } else {
                map.put(key, new StatsDto(key, total, done));
            }
        }

        // 3. 리스트로 변환 및 키 기준 정렬
        List<StatsDto> result = new ArrayList<>(map.values());
        result.sort((a, b) -> a.getKey().compareTo(b.getKey()));
        return result;
    }
}