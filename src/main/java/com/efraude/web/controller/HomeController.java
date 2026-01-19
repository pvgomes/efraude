package com.efraude.web.controller;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class HomeController {

    private final FraudService fraudService;

    @GetMapping({"/home", "/"})
    public String home(Model model) {
        // Get recent frauds
        Page<Fraud> recentFrauds = fraudService.findActive(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        model.addAttribute("recentFrauds", recentFrauds.getContent());
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
