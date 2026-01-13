package com.example.market.service;

import com.example.market.dto.MarketPostRequestDto;
import com.example.market.dto.MarketPostResponseDto;
import com.example.market.dto.MarketPostSearchDto;
import com.example.market.model.MarketPost;
import com.example.market.model.PostFile;
import com.example.market.repository.MarketPostRepository;
import com.example.market.repository.PostFileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketPostService {

    private final MarketPostRepository marketPostRepository;
    private final PostFileRepository postFileRepository;

    private final String uploadPath = "C:/eco_images/";

    // 1. 목록 조회 (페이징 + 검색)
    @Transactional(readOnly = true)
    public Page<MarketPostResponseDto> getPaging(int page, String sortStr, MarketPostSearchDto searchDto) {

        // 정렬 조건 설정
        Sort sort;
        switch (sortStr) {
            case "volume": sort = Sort.by(Sort.Direction.DESC, "volumeKwh"); break;
            case "priceHigh": sort = Sort.by(Sort.Direction.DESC, "priceKrw"); break;
            case "priceLow": sort = Sort.by(Sort.Direction.ASC, "priceKrw"); break;
            default: sort = Sort.by(Sort.Direction.DESC, "postId"); break;
        }
        Pageable pageable = PageRequest.of(page, 10, sort);

        // 검색 조건 적용
        Specification<MarketPost> spec = MarketPostSpecService.search(searchDto);

        // 조회
        Page<MarketPost> postPage = marketPostRepository.findAll(spec, pageable);

        // [수정] DTO 변환 (이미지 로직이 DTO 안으로 이동했으므로 서비스 코드가 깔끔해짐)
        return postPage.map(MarketPostResponseDto::fromEntity);
    }

    // 2. 상세 조회
    @Transactional(readOnly = true)
    public MarketPostResponseDto getPostById(Long id) {
        MarketPost post = marketPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        // DTO 변환 시 post.getFiles()를 통해 이미지를 가져옴
        return MarketPostResponseDto.fromEntity(post);
    }

    // 3. 게시글 등록
    @Transactional
    public void createPost(MarketPostRequestDto dto) {
        Long mockSellerId = 1L; // 임시 ID

        // (1) 게시글 먼저 저장 (ID 생성을 위해)
        // toEntity에서 imageUrl 파라미터가 제거되었습니다.
        MarketPost marketPost = dto.toEntity(mockSellerId);
        MarketPost savedPost = marketPostRepository.save(marketPost);

        // (2) 파일이 있다면 저장 후 PostFile 테이블에 등록
        MultipartFile file = dto.getImageFile();
        if (file != null && !file.isEmpty()) {
            savePostFile(savedPost.getPostId(), file);
        }
    }

    // 4. 게시글 수정
    @Transactional
    public void updatePost(Long postId, MarketPostRequestDto dto) {
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 새 이미지가 업로드되었다면?
        MultipartFile file = dto.getImageFile();
        if (file != null && !file.isEmpty()) {

            // 기존 파일들을 모두 '삭제(Y)' 처리
            // post.getFiles()로 연결된 파일 목록을 가져와서 하나씩 상태를 변경합니다.
            List<PostFile> oldFiles = post.getFiles();
            for (PostFile oldFile : oldFiles) {
                oldFile.delete(); // 여기서 엔티티의 delYn이 "Y"로 바뀝니다.
            }
            // 새 파일 저장
            savePostFile(postId, file);
        }
        // 게시글 내용 업데이트
        post.update(
                dto.getTitle(), dto.getEnergyType(), dto.getLandType(), dto.getLandArea(),
                dto.getLocation(), dto.getLocationDetail(), dto.getFacilityCapacity(),
                dto.getWeightingFactor(), dto.getVolumeKwh(), dto.getVolumeRec(),
                dto.getContractType(), dto.getContractUnit(), dto.getPriceKrw(),
                "on".equals(dto.getIsPriceNegotiable()) ? "Y" : "N",
                dto.getContractStartDate(), dto.getContractEndDate(),
                "on".equals(dto.getIsPeriodNegotiable()) ? "Y" : "N",
                dto.getContent()
        );
    }

    // [공통] 파일 저장 로직 분리
    private void savePostFile(Long postId, MultipartFile file) {
        try {
            File directory = new File(uploadPath);
            if (!directory.exists()) directory.mkdirs();

            String originalName = file.getOriginalFilename();
            String storedName = UUID.randomUUID() + "_" + originalName;

            // 실제 파일 저장
            file.transferTo(new File(uploadPath, storedName));

            // DB 저장
            PostFile postFile = PostFile.builder()
                    .postId(postId)
                    .originalFileName(originalName)
                    .storedFileName(storedName)
                    .delYn("N")
                    .build();
            postFileRepository.save(postFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 실패"); // 트랜잭션 롤백을 위해 런타임 예외 발생
        }
    }

    @Transactional
    public void deletePost(Long postId) {
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));

        post.delete(); // delYn = "Y"로 변경 (Soft Delete)
    }

    // [추가] 판매 상태 변경 (판매중 <-> 판매종료)
    @Transactional
    public void updateStatus(Long postId, String status) {
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));
        // Entity에 setSaleYn 메서드가 없으면 추가하거나, updateStatus 메서드를 만드세요.
        // 여기서는 Lombok @Setter가 있다고 가정하고 바로 넣습니다.
        post.setSaleYn(status);
    }
}