package com.example.gatewayservice.util;

public class EmailCodeUtil {
        public static String generate() {
            return String.valueOf((int)(Math.random() * 900000) + 100000);
        }
    }