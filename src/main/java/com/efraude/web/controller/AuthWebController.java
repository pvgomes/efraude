package com.efraude.web.controller;

import com.efraude.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/auth")
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password. Please make sure your email is verified.");
        }
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.registerUser(name, email, password);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please check your email to confirm your account.");
            return "redirect:/web/auth/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/auth/signup";
        }
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token, Model model) {
        boolean confirmed = authService.confirmEmail(token);
        model.addAttribute("confirmed", confirmed);
        return "auth/confirm";
    }

    @GetMapping("/resend")
    public String resendPage() {
        return "auth/resend";
    }

    @PostMapping("/resend")
    public String resend(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            authService.resendConfirmationEmail(email);
            redirectAttributes.addFlashAttribute("success", "Confirmation email has been resent. Please check your inbox.");
            return "redirect:/web/auth/resend";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/auth/resend";
        }
    }
}
