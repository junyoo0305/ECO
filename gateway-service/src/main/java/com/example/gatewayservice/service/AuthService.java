package com.example.gatewayservice.service;

import com.example.gatewayservice.dto.LoginRequest;
import com.example.gatewayservice.dto.LoginResponse;
import com.example.gatewayservice.dto.RegisterRequest;
import com.example.gatewayservice.entity.User;
import com.example.gatewayservice.exception.InvalidPasswordException;
import com.example.gatewayservice.repository.UserRepository;
import com.example.gatewayservice.util.JwtUtil;
import com.example.gatewayservice.util.Sha512Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Mono<LoginResponse> login(LoginRequest request) {
        return Mono.fromCallable(() ->
                userRepository.findBySellComId(request.getSellComId())
                        .orElseThrow(() -> new RuntimeException("User not found"))
        ).map(user -> {
            // 1. 회원 유형 일치 확인 (String 변환 비교)
            String reqType = String.valueOf(request.getCompanyType());
            String userType = String.valueOf(user.getCompanyType());

            if (!reqType.equals(userType)) {
                throw new IllegalStateException("회원 유형이 일치하지 않습니다.");
            }

            // 2. 승인 대기 확인 (판매자인 경우만)
            // (만약 DB에 company_type이 "1" 또는 "SELLER"로 저장되어 있다면 그 값에 맞춰 수정)
            if (("1".equals(userType) || "SELLER".equals(userType)) && !"Y".equals(user.getApprovYn())) {
                throw new IllegalStateException("관리자 승인이 필요한 계정입니다.");
            }

            // 3. 비밀번호 검증
            String hashed = Sha512Util.hash(request.getSellComPw(), user.getSalt());
            if (!hashed.equals(user.getSellComPw())) {
                throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
            }

            // [핵심] 토큰에 넣을 최종 역할(Role) 결정 로직
            String tokenRole = user.getRole(); // 기본적으로 DB의 role ("USER" or "ADMIN") 가져옴

            if ("USER".equals(tokenRole)) {
                // 관리자가 아니라면, company_type을 보고 실제 역할을 결정
                if ("1".equals(userType)) {
                    tokenRole = "SELLER";
                } else if ("0".equals(userType)) {
                    tokenRole = "BUYER";
                } else {
                    // DB에 "SELLER", "BUYER" 문자열로 저장된 경우 그대로 사용
                    tokenRole = userType;
                }
            }

            // 4. 결정된 역할(tokenRole)을 토큰에 심어줌 -> 이제 게이트웨이가 "SELLER"라고 인식함!
            String token = jwtUtil.generateToken(user.getSellComId(), tokenRole);

            return new LoginResponse(token, user.getSellComId(), tokenRole);
        });
    }

    // 아이디 중복 에러 로직
    public boolean existsSellComId(String sellComId) {
        return userRepository.findBySellComId(sellComId).isPresent();
    }


    public Mono<Void> register(RegisterRequest request) {
        return Mono.fromRunnable(() -> {
            // 아이디 중복 로직
            if (userRepository.findBySellComId(request.getSellComId()).isPresent()) {
                throw new RuntimeException("Duplicate user");
            }
            // 비밀번호 확인 로직
            if (!request.getSellComPw().equals(request.getSellComPwConfirm())) {
                throw new RuntimeException("Password does not match");
            }


            String salt = Sha512Util.generateSalt();
            String hashed = Sha512Util.hash(request.getSellComPw(), salt);

            User user = new User();
            user.setCompanyType(request.getCompanyType()); // 구매자 or 판매자 타입

            user.setSellComName(request.getSellComName()); // 사업자명
            user.setSellRegNum(request.getSellRegNum()); // 사업자 등록 번호
            user.setSellRepName(request.getSellRepName()); // 대표자 성명
            user.setSellComBirth(request.getSellComBirth()); // 개업일자
            user.setSellComAdr(request.getSellComAdr()); // 회사 주소
            user.setSellComNum(request.getSellComNum()); // 회사 전화번호
            user.setSellBmName(request.getSellBmName()); // 담당자 이름
            user.setSellBmNum(request.getSellBmNum()); // 담당자 전화번호
            user.setSellBmDep(request.getSellBmDep()); // 담당자 부서
            user.setSellComId(request.getSellComId()); // Id
            user.setSellComPw(hashed); // Password
            user.setSalt(salt);
            user.setSellComEmail(request.getSellComEmail()); // Email
            user.setMarketingCheck(request.getMarketingCheck());
            user.setRole("USER"); // Admin or User 구분
            user.setApprovYn("N"); // 승인 대기

            userRepository.save(user);
        });
    }

}
