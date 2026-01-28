package com.example.seller.mypage.service;

import com.example.seller.mypage.dto.CorporateMemberResponseDto;
import com.example.seller.mypage.dto.PasswordUpdateDto;
import com.example.seller.mypage.model.SubManager;
import com.example.seller.mypage.model.User; // ★ seller 패키지의 User를 사용해야 함
import com.example.seller.mypage.repository.SubManagerRepository;
import com.example.seller.mypage.repository.UserRepository; // ★ seller 패키지의 Repo를 사용해야 함
import com.example.seller.util.Sha512Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SellerMyPageService {

    private final UserRepository userRepository;
    private final SubManagerRepository subManagerRepository;
    private final Sha512Util sha512Util;

    // 기업 정보 조회
    @Transactional(readOnly = true)
    public CorporateMemberResponseDto getMyInfo(String sellComId) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        return CorporateMemberResponseDto.fromEntity(user);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String sellComId, PasswordUpdateDto dto) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // 현재 비밀번호 확인 (SHA-512)
        // 사용자의 Salt를 꺼내서 입력받은 비밀번호를 똑같이 암호화해본 뒤 비교
        String hashedCurrentPw = sha512Util.encrypt(dto.getCurrentPassword(), user.getSalt());

        if (!hashedCurrentPw.equals(user.getSellComPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 확인
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 정책 검증
        validatePassword(dto.getNewPassword());

        // 새 비밀번호 암호화 및 저장
        // (보안을 위해 비밀번호 변경 시 Salt도 새로 발급하는 것을 권장합니다)
        String newSalt = sha512Util.generateSalt();
        String encryptedNewPw = sha512Util.encrypt(dto.getNewPassword(), newSalt);

        user.setSalt(newSalt);        // 새 솔트 저장
        user.setSellComPw(encryptedNewPw); // 새 비밀번호 저장
    }

    private void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 30) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 30자 이내여야 합니다.");
        }
        // 영문(대소문자), 숫자, 특수문자 포함
        String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\W_]).{8,30}$";
        if (!Pattern.matches(pattern, password)) {
            throw new IllegalArgumentException("비밀번호는 영문(대소문자), 숫자, 특수문자를 모두 포함해야 합니다.");
        }
    }

    // 담당자 관리

    @Transactional(readOnly = true)
    public List<SubManager> getManagers(String sellComId) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // User의 PK(Long id)를 사용하여 담당자 조회
        return subManagerRepository.findByUserIdAndDelYn(user.getId(), "N");
    }

    @Transactional
    public void addManager(String sellComId, String name, String email, String dept, String phone) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        SubManager manager = SubManager.builder()
                .userId(user.getId())
                .managerName(name)
                .email(email)
                .department(dept)
                .phone(phone)
                .delYn("N")
                .build();

        subManagerRepository.save(manager);
    }

    @Transactional
    public void updateManager(String sellComId, Long subManagerId, String name, String email, String dept, String phone) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        SubManager manager = subManagerRepository.findById(subManagerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 담당자입니다."));

        // [보안] 내 담당자가 맞는지 확인
        if (!manager.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        manager.setManagerName(name);
        manager.setDepartment(dept);
        manager.setPhone(phone);
        manager.setEmail(email);
    }

    @Transactional
    public void deleteManager(String sellComId, Long subManagerId) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        SubManager manager = subManagerRepository.findById(subManagerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 담당자입니다."));

        // [보안] 내 담당자가 맞는지 확인
        if (!manager.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        manager.setDelYn("Y");
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawSeller(String sellComId) {
        User user = userRepository.findBySellComId(sellComId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        userRepository.delete(user);
    }
}