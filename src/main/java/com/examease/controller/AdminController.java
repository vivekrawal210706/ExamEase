package com.examease.controller;

import com.examease.entity.Exam;
import com.examease.repository.ExamRepository;
import com.examease.repository.ResultRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminController {

    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;

    @Value("${examease.admin.username:admin}")
    private String adminUsername;

    @Value("${examease.admin.password:ExamEase@123}")
    private String adminPassword;

    public AdminController(ExamRepository examRepository,
                           ResultRepository resultRepository) {
        this.examRepository = examRepository;
        this.resultRepository = resultRepository;
    }

    // Display admin login page
    @GetMapping("/admin/login")
    public String showAdminLogin() {
        return "admin-login";
    }

    // Check admin login details
    @PostMapping("/admin/login")
    public String loginAdmin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        if (adminUsername.equals(username)
                && adminPassword.equals(password)) {

            session.setAttribute("adminLoggedIn", true);
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", "Incorrect username or password.");
        return "admin-login";
    }

    // Display admin dashboard
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(HttpSession session,
                                     Model model) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        Map<Long, String> examNames = new HashMap<>();

        for (Exam exam : examRepository.findAll()) {
            examNames.put(exam.getId(), exam.getTitle());
        }

        model.addAttribute("results", resultRepository.findAll());
        model.addAttribute("examNames", examNames);

        return "admin-dashboard";
    }

    // Log out the admin
    @GetMapping("/admin/logout")
    public String logoutAdmin(HttpSession session) {
        session.removeAttribute("adminLoggedIn");
        return "redirect:/admin/login";
    }
}
