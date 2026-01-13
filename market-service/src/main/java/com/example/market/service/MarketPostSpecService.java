package com.example.market.service; // 또는 repository

import com.example.market.dto.MarketPostSearchDto;
import com.example.market.model.MarketPost;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MarketPostSpecService {

    public static Specification<MarketPost> search(MarketPostSearchDto condition) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 삭제되지 않은 글만 (기본 조건)
            predicates.add(builder.equal(root.get("delYn"), "N"));

            // 2. 계약 유형 (하나라도 포함되면 OK)
            // DB에는 "REC 현물,직접 PPA" 처럼 쉼표로 저장되어 있으므로 like 검색 필요
            if (condition.getContractTypes() != null && !condition.getContractTypes().isEmpty()) {
                List<Predicate> contractPredicates = new ArrayList<>();
                for (String type : condition.getContractTypes()) {
                    contractPredicates.add(builder.like(root.get("contractType"), "%" + type + "%"));
                }
                predicates.add(builder.or(contractPredicates.toArray(new Predicate[0])));
            }

            // 3. 발전원 (태양광, 풍력 등 - 정확히 일치)
            if (condition.getEnergyTypes() != null && !condition.getEnergyTypes().isEmpty()) {
                predicates.add(root.get("energyType").in(condition.getEnergyTypes()));
            }

            // 4. 생산 지역 (주소의 앞부분으로 검색)
            if (condition.getRegions() != null && !condition.getRegions().isEmpty()) {
                List<Predicate> regionPredicates = new ArrayList<>();
                for (String region : condition.getRegions()) {
                    regionPredicates.add(builder.like(root.get("location"), region + "%"));
                }
                predicates.add(builder.or(regionPredicates.toArray(new Predicate[0])));
            }

            // 5. 가격 범위 (min ~ max)
            if (condition.getMinPrice() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("priceKrw"), condition.getMinPrice()));
            }
            if (condition.getMaxPrice() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("priceKrw"), condition.getMaxPrice()));
            }

            // 6. 협의 가능만 보기
            if (Boolean.TRUE.equals(condition.getIsNego())) {
                predicates.add(builder.equal(root.get("isPriceNegotiable"), "Y"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}