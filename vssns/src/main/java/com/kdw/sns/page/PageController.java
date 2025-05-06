package com.kdw.sns.page;


import com.kdw.sns.auth.dto.request.OAuthSignupDto;
import com.kdw.sns.auth.dto.request.SignupDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    // ✅ 로그인 성공 시 이동하는 메인 페이지
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "로그인 성공! 메인 페이지입니다.");
        return "home"; // → templates/home.html
    }


    // 소셜 회원가입
    @GetMapping("/oauth/signup")
    public String oauthSignup(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "provider") String provider,
            @RequestParam(name = "providerId") String providerId,
            Model model) {

        model.addAttribute("oauthSignupDto", OAuthSignupDto.builder()
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .build());

        return "oauth_signup";
    }

    // 일반 회원가입
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "signup";
    }

    @GetMapping("/oauth/login")
    public String loginPage() {
        return "oauth_login";
    }


}