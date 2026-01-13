package com.example.seller.selpost.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_files")
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_FILE_ID")
    private Long id;

    @Column(name = "POST_ID")
    private Long postId; // 어떤 게시글의 사진인지 (외래키)

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "STORED_FILE_NAME")
    private String storedFileName; // 실제 저장된 이름 (UUID 포함)
    @Column(name = "DEL_YN")
    private String delYn;
}