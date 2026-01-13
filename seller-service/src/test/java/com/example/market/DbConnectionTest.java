package com.example.market;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootTest // ★ 중요: 이 어노테이션이 있어야 스프링이 실행되고 DB에 연결됩니다.
public class DbConnectionTest {

    @Autowired
    private DataSource dataSource; // application.properties에 설정한 정보로 DB 객체 주입

    @Test
    @DisplayName("1. DB 연결 확인 테스트")
    public void connectionTest() {
        try (Connection connection = dataSource.getConnection()) {

            // 1. 연결 객체 정보 출력
            System.out.println("\n==================================================");
            System.out.println(">>> DB 연결 성공! (Connection Success)");
            System.out.println(">>> Connection Class: " + connection.getClass());

            // 2. DB 메타데이터(URL, User 등) 확인
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(">>> DB URL: " + metaData.getURL());
            System.out.println(">>> DB User: " + metaData.getUserName());
            System.out.println(">>> DB Product: " + metaData.getDatabaseProductName());
            System.out.println("==================================================\n");

        } catch (SQLException e) {
            System.out.println("\n##################################################");
            System.out.println(">>> DB 연결 실패! (Connection Failed)");
            System.out.println(">>> 에러 메시지: " + e.getMessage());
            System.out.println("##################################################\n");

            // 테스트를 실패 상태로 만듦
            throw new RuntimeException(e);
        }
    }
}