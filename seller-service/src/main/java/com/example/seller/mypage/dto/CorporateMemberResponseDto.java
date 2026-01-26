package com.example.seller.mypage.dto;

import com.example.seller.mypage.model.User; // [중요] User 엔티티 import 확인 필요
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorporateMemberResponseDto {
    private String sellComName;   // 회사명
    private String sellRegNum;    // 사업자번호
    private String sellRepName;   // 대표자명
    private String sellComAdr;    // 주소
    private String sellComNum;    // 전화번호
    private String sellComEmail;  // 이메일

    private String sellComId;     // 아이디

    private String sellBmName;    // 담당자명
    private String sellBmNum;     // 담당자 연락처
    private String sellBmDep;     // 담당자 부서

    // [수정] User 엔티티를 받아서 DTO로 변환
    public static CorporateMemberResponseDto fromEntity(User entity) {
        return CorporateMemberResponseDto.builder()
                .sellComName(entity.getSellComName())
                .sellRegNum(entity.getSellRegNum())
                .sellRepName(entity.getSellRepName())
                .sellComAdr(entity.getSellComAdr())
                .sellComNum(entity.getSellComNum())
                .sellComEmail(entity.getSellComEmail())
                .sellComId(entity.getSellComId())
                .sellBmName(entity.getSellBmName())
                .sellBmNum(entity.getSellBmNum())
                .sellBmDep(entity.getSellBmDep())
                .build();
    }
}