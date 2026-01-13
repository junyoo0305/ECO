package com.example.seller.mypage.repository;

import com.example.seller.mypage.model.CorporateMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateMemberRepository extends JpaRepository<CorporateMember, Long> {
    // 기본 제공되는 findById(Long id)만 사용하면 되므로 추가 코드는 필요 없습니다.
}