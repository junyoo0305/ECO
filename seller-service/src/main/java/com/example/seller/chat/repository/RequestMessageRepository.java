package com.example.seller.chat.repository;

import com.example.seller.chat.model.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestMessageRepository extends JpaRepository<RequestMessage, Long> {
    // 특정 메시지 찾기 등 필요하면 추가
}