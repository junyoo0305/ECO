package com.example.gatewayservice.config;

import com.example.gatewayservice.entity.CompanyType;
import com.example.gatewayservice.entity.User;
import com.example.gatewayservice.repository.UserRepository;
import com.example.gatewayservice.util.Sha512Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {

            // ADMIN
            String adminSalt = Sha512Util.generateSalt();
            User admin = new User();
            admin.setSellComName("관리자회사");
            admin.setSellRegNum("0000000000");
            admin.setSellRepName("관리자");
            admin.setSellComBirth("20010101");
            admin.setSellComAdr("성남시");
            admin.setSellComNum("010-0000-9999");
            admin.setSellBmName("관리자회사담당인");
            admin.setSellBmNum("00009998");
            admin.setSellBmDep("인사팀");
            admin.setSellComId("admin");
            admin.setSalt(adminSalt);
            admin.setSellComPw(Sha512Util.hash("admin123", adminSalt));
            admin.setSellComEmail("admin@example.com");
            admin.setRole("ADMIN");
            admin.setCompanyType(CompanyType.BUYER);
            admin.setApprovYn("Y"); // 관리자는 바로 승인
            admin.setMarketingCheck("Y");
            userRepository.save(admin);

            // USER1
            String salt1 = Sha512Util.generateSalt();
            User user1 = new User();
            user1.setSellComName("유저1회사");
            user1.setSellRegNum("0000000001");
            user1.setSellRepName("유저1");
            user1.setSellComBirth("20010101");
            user1.setSellComAdr("성남시");
            user1.setSellComNum("010-1111-2222");
            user1.setSellBmName("유저1회사담당인");
            user1.setSellBmNum("010-1111-2221");
            user1.setSellBmDep("인사팀");
            user1.setSellComId("user1");
            user1.setSalt(salt1);
            user1.setSellComPw(Sha512Util.hash("user123", salt1));
            user1.setSellComEmail("user1@example.com");
            user1.setRole("USER");
            user1.setCompanyType(CompanyType.BUYER);
            user1.setApprovYn("Y");// 테스트용으로 승인
            user1.setMarketingCheck("Y");
            userRepository.save(user1);

            // USER2
            String salt2 = Sha512Util.generateSalt();
            User user2 = new User();
            user2.setSellComName("유저2회사");
            user2.setSellRegNum("0000000002");
            user2.setSellRepName("유저2");
            user2.setSellComBirth("20010101");
            user2.setSellComAdr("성남시");
            user2.setSellComNum("010-2222-3333");
            user2.setSellBmName("유저2회사담당인");
            user2.setSellBmNum("010-2222-3332");
            user2.setSellBmDep("인사팀");
            user2.setSellComId("user2");
            user2.setSalt(salt2);
            user2.setSellComPw(Sha512Util.hash("user123", salt2));
            user2.setSellComEmail("user2@example.com");
            user2.setRole("USER");
            user2.setCompanyType(CompanyType.SELLER);
            user2.setApprovYn("Y"); // 테스트용으로 승인
            user2.setMarketingCheck("Y");
            userRepository.save(user2);
        }
    }
}
