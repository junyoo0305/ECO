package com.example.inquiry.repository;


import com.example.inquiry.model.Inquiry;
import com.example.inquiry.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // ▼▼▼ [파라미터 타입 확인] Long inquirerId여야 합니다. ▼▼▼
    List<Inquiry> findByInquirerIdOrderByCreatedDateDesc(Long inquirerId);

    // ▼▼▼ [파라미터 타입 확인] Long recipientId여야 합니다. ▼▼▼
    List<Inquiry> findByRecipientIdOrderByCreatedDateDesc(Long recipientId);

    // 여기는 게시글 번호니까 원래 Long이 맞습니다.
    List<Inquiry> findByPostIdAndPostTypeOrderByCreatedDateDesc(Long postId, PostType postType);
}