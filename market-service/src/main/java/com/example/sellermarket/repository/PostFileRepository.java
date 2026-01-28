package com.example.sellermarket.repository;

import com.example.sellermarket.model.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    // [추가] 게시글 ID로 첫 번째 사진 한 장만 가져오기
    PostFile findFirstByPostIdOrderByIdAsc(Long postId);
}