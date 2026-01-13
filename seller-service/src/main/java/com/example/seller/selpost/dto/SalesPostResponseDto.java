package com.example.seller.selpost.dto;

import com.example.seller.selpost.model.PostFile;
import com.example.seller.selpost.model.SalesPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesPostResponseDto {
    private Long id;
    private String title;
    private String energyType;
    private String status;
    private Long priceKrw;
    private String imageUrl;
    private LocalDateTime regDate;

    public static SalesPostResponseDto fromEntity(SalesPost entity) {

        // 삭제되지 않은('N') 파일 중 첫 번째 사진만 가져오기
        String thumbUrl = null;
        List<PostFile> files = entity.getFiles();
        if (files != null && !files.isEmpty()) {
            thumbUrl = files.stream()
                    // 1. 삭제 여부(delYn)가 'N'인 것만 필터링
                    .filter(file -> "N".equals(file.getDelYn()))
                    // 2. 그 중 첫 번째 파일 선택
                    .findFirst()
                    // 3. 경로 문자열로 변환
                    .map(file -> "/market/images/" + file.getStoredFileName())
                    // 4. 없으면 null
                    .orElse(null);
        }

        return SalesPostResponseDto.builder()
                .id(entity.getPostId())
                .title(entity.getTitle())
                .energyType(entity.getEnergyType())
                .priceKrw(entity.getPriceKrw())

                // [수정] 위에서 만든 주소 넣기
                .imageUrl(thumbUrl)

                .regDate(entity.getRegDate())
                .status("Y".equals(entity.getSaleYn()) ? "판매중" : "판매종료")
                .build();
    }
}