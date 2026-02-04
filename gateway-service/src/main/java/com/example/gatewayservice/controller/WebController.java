package com.example.gatewayservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "기업 재생에너지 메인 페이지");
        return "main";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "로그인");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원 가입");
        return "register";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }

    @GetMapping("/memberlist")
    public String memberlist(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "memberlist";
    }

    @GetMapping("/adminlogin")
    public String adminlogin(Model model) {
        model.addAttribute("message", "관리자 로그인");
        return "adminlogin";
    }


    @GetMapping("/memberwrite")
    public String memberwrite(Model model) {
        model.addAttribute("message", "관리자 등록");
        return "memberwrite";
    }

    @GetMapping("/memberview")
    public String memberview(Model model) {
        model.addAttribute("message", "관리자 수정");
        return "memberview";
    }

    @GetMapping("/sellerlist")
    public String sellerlist(Model model) {
        model.addAttribute("message", "판매회원 관리");
        return "sellerlist";
    }

    @GetMapping("/sellerview")
    public String sellerview(Model model) {
        model.addAttribute("message", "판매회원 상세");
        return "sellerview";
    }
} 