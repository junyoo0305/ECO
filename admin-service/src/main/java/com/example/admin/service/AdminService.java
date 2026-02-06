package com.example.admin.service;

import com.example.admin.dto.AdminRequestDto;
import com.example.admin.model.Admin;
import com.example.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // SHA-512 암호화 메소드
    private String encryptSHA512(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : md.digest()) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    //  로그인 인증 로직
    public Admin login(String userId, String password) {
        // 1. 아이디로 조회
        Admin admin = adminRepository.findByUserId(userId)
                .orElse(null); // 없으면 null 반환

        // 2. 회원이 없거나 비밀번호가 틀리면 null 반환
        if (admin == null || !admin.getPassword().equals(encryptSHA512(password))) {
            return null;
        }

        // 3. 인증 성공 시 회원 객체 반환
        return admin;
    }

    // [추가] 아이디 중복 확인 서비스 로직
    @Transactional(readOnly = true)
    public boolean checkIdDuplicate(String userId) {
        return adminRepository.existsByUserId(userId);
    }

    @Transactional
    public Long save(AdminRequestDto requestDto) {
        // 비번 암호화
        String rawPassword = requestDto.getPassword();
        String encPassword = encryptSHA512(rawPassword);
        requestDto.setPassword(encPassword);
        return adminRepository.save(requestDto.toEntity()).getSeq();
    }

    @Transactional(readOnly = true)
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Admin findById(Long seq) {
        return adminRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
    }

    // 정보 수정
    @Transactional
    public void update(Long seq, AdminRequestDto requestDto) {
        Admin admin = adminRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
        //비밀번호가 입력되었을 때만 암호화하여 변경
        if (requestDto.getPassword() != null && !requestDto.getPassword().trim().isEmpty()) {
            String encPassword = encryptSHA512(requestDto.getPassword());
            admin.setPassword(encPassword);
        }
        // 비밀번호가 입력된 경우에만 변경, 아니면 기존 유지 logic 등 필요시 추가
        // 여기서는 편의상 DTO의 모든 필드로 업데이트한다고 가정
        // (실무에선 Dirty Checking을 위해 Setter나 별도 update 메소드 사용 권장)
        admin.setName(requestDto.getName());
        admin.setEmail(requestDto.getEmail());
        admin.setDepartment(requestDto.getDepartment());
        admin.setPhone(requestDto.getPhone());
        admin.setAuth(requestDto.getAuth());
    }

    // 삭제 (Soft Delete: DEL_YN = 'Y')
    @Transactional
    public void delete(Long seq) {
        Admin admin = adminRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
        admin.setDelYn("Y"); // 삭제 플래그 업데이트
    }
}