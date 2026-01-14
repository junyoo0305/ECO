package com.example.seller.mypage.service;

import com.example.seller.mypage.dto.CorporateMemberResponseDto;
import com.example.seller.mypage.dto.PasswordUpdateDto;
import com.example.seller.mypage.model.CorporateMember;
import com.example.seller.mypage.model.SubManager;
import com.example.seller.mypage.repository.CorporateMemberRepository;
import com.example.seller.mypage.repository.SubManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SellerMyPageService {

    private final CorporateMemberRepository corporateMemberRepository;
    private final SubManagerRepository subManagerRepository;

    // 기업 정보 및 비밀번호 관리 (본인 계정)

    // 내 정보 조회
    @Transactional(readOnly = true)
    public CorporateMemberResponseDto getMyInfo(Long sellerId) {
        CorporateMember member = corporateMemberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        return CorporateMemberResponseDto.fromEntity(member);
    }

    // 비밀번호 변경 (검증 로직 포함)
    @Transactional
    public void changePassword(Long sellerId, PasswordUpdateDto dto) {
        CorporateMember member = corporateMemberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // 현재 비밀번호 일치 확인
        if (!member.getSellComPw().equals(dto.getCurrentPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호와 확인 비밀번호 일치 확인 (DTO에 필드가 있다고 가정)
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 복잡도 검증 (8~30자, 영문 대소문자/숫자/특수문자 포함)
        validatePassword(dto.getNewPassword());

        // 모든 검증 통과 시 변경 수행
        member.updatePassword(dto.getNewPassword());
    }

    // 정규식을 사용하여 비밀번호 규칙 확인
    private void validatePassword(String password) {
        // 길이 체크 (8~30자)
        if (password.length() < 8 || password.length() > 30) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 30자 이내여야 합니다.");
        }
        // 정규식 체크
        // (?=.*[0-9]): 숫자 적어도 1개
        // (?=.*[a-z]): 소문자 적어도 1개
        // (?=.*[A-Z]): 대문자 적어도 1개
        // (?=.*[\W_]): 특수문자 적어도 1개
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,30}$";

        if (!Pattern.matches(pattern, password)) {
            throw new IllegalArgumentException("비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
        }
    }



    // 담당자 관리 (Sub Manager)

    // 담당자 목록 조회
    @Transactional(readOnly = true)
    public List<SubManager> getManagers(Long sellerId) {
        return subManagerRepository.findBySellerIdAndDelYn(sellerId, "N");
    }

    // 담당자 추가
    @Transactional
    public void addManager(Long sellerId, String name, String email, String dept, String phone) {
        SubManager manager = SubManager.builder()
                .sellerId(sellerId)
                .managerName(name)
                .email(email)
                .department(dept)
                .phone(phone)
                .delYn("N")
                .build();
        subManagerRepository.save(manager);
    }

    // 담당자 수정
    @Transactional
    public void updateManager(Long subManagerId, String name, String email, String dept, String phone) {
        // 수정할 대상 찾기
        SubManager manager = subManagerRepository.findById(subManagerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 담당자입니다."));
        // 정보 덮어쓰기 (Entity에 @Setter가 있어야 합니다)
        manager.setManagerName(name);
        manager.setDepartment(dept);
        manager.setPhone(phone);
        manager.setEmail(email);
        // 따로 save()를 호출하지 않아도, @Transactional이 있으면 자동으로 Update 됩니다.
    }

    // 담당자 삭제 (Soft Delete)
    @Transactional
    public void deleteManager(Long subManagerId) {
        SubManager manager = subManagerRepository.findById(subManagerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 담당자입니다."));

        manager.setDelYn("Y"); // 상태만 변경
    }

    // 회원탈퇴
    @Transactional
    public void withdrawSeller(Long sellerId) {
        // 존재 여부 확인 (없으면 에러)
        if (!corporateMemberRepository.existsById(sellerId)) {
            throw new IllegalArgumentException("회원 정보가 없습니다.");
        }
        corporateMemberRepository.deleteById(sellerId);
    }
}