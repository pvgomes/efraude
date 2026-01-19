package com.efraude.web.controller;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/web/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FraudService fraudService;

    @GetMapping("/moderation")
    public String moderation(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Fraud> frauds = fraudService.findAll(PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt")));
        model.addAttribute("frauds", frauds);
        model.addAttribute("currentPage", page);
        return "admin/moderation";
    }

    @PostMapping("/frauds/{id}/archive")
    public String archiveFraud(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            fraudService.archiveFraud(id);
            redirectAttributes.addFlashAttribute("success", "Fraud archived successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/admin/moderation";
    }
}
