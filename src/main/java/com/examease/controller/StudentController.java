
package com.examease.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @GetMapping("/student-entry")
    public String studentEntry() {
        return "student-entry";
    }

    @PostMapping("/student-entry")
    public String submitDetails(
            @RequestParam String name,
            @RequestParam String rollNumber,
            HttpSession session,
            Model model) {

        if (name.trim().isEmpty() || rollNumber.trim().isEmpty()) {
            model.addAttribute("error", "Please enter both details.");
            return "student-entry";
        }

        session.setAttribute("studentName", name.trim());
        session.setAttribute("rollNumber", rollNumber.trim());

        return "redirect:/exams";
    }
}

