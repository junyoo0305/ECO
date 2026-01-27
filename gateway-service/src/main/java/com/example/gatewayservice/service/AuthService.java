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
            // 1️⃣ 회원 유형 불일치 (프론트에서 잘못 선택)
            if (user.getCompanyType() != request.getCompanyType()) {
                throw new IllegalStateException("회원 유형이 일치하지 않습니다.");
            }
            // 2️⃣ 승인 여부 (SELLER만 의미 있음)
            if (!"Y".equals(user.getApprovYn())) {
                throw new IllegalStateException("승인 대기 중인 계정입니다.");
            }
            // 3️⃣ 비밀번호 검증 (가장 비용 큰 연산)
            String hashed = Sha512Util.hash(
                    request.getSellComPw(),
                    user.getSalt()
            );
            if (!hashed.equals(user.getSellComPw())) {
                throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
            }
            // 4️⃣ 토큰 발급
            String token = jwtUtil.generateToken(user.getSellComId());
            return new LoginResponse(token, user.getSellComId(), user.getRole());
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
