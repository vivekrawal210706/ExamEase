package com.examease.controller;

import com.examease.entity.Exam;
import com.examease.entity.Question;
import com.examease.entity.Result;

import com.examease.repository.ExamRepository;
import com.examease.repository.QuestionRepository;
import com.examease.repository.ResultRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class ExamController {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    public ExamController(ExamRepository examRepository,
                          QuestionRepository questionRepository,
                          ResultRepository resultRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    // Display available examinations
    @GetMapping("/exams")
    public String showExams(HttpSession session, Model model) {

        String studentName =
                (String) session.getAttribute("studentName");

        if (studentName == null) {
            return "redirect:/student-entry";
        }

        model.addAttribute("studentName", studentName);
        model.addAttribute("rollNumber",
                session.getAttribute("rollNumber"));

        model.addAttribute("exams", examRepository.findAll());

        return "exams";
    }

    // Start an examination
    @GetMapping("/start-exam/{id}")
    public String startExam(@PathVariable Long id,
                            HttpSession session,
                            Model model) {

        if (session.getAttribute("studentName") == null) {
            return "redirect:/student-entry";
        }

        Exam exam = examRepository.findById(id).orElse(null);

        if (exam == null) {
            return "redirect:/exams";
        }

        List<Question> questions =
                questionRepository.findByExamId(id);

        if (questions.isEmpty()) {
            return "redirect:/exams";
        }

        model.addAttribute("exam", exam);
        model.addAttribute("questions", questions);

        return "exam-page";
    }

    // Submit examination and calculate result
    @PostMapping("/submit-exam/{id}")
    public String submitExam(@PathVariable Long id,
                             @RequestParam Map<String, String> answers,
                             HttpSession session) {

        String studentName =
                (String) session.getAttribute("studentName");

        String rollNumber =
                (String) session.getAttribute("rollNumber");

        if (studentName == null || rollNumber == null) {
            return "redirect:/student-entry";
        }

        Exam exam = examRepository.findById(id).orElse(null);

        if (exam == null) {
            return "redirect:/exams";
        }

        List<Question> questions =
                questionRepository.findByExamId(id);

        int score = 0;

        for (Question question : questions) {

            String selectedAnswer =
                    answers.get("q_" + question.getId());

            if (selectedAnswer != null &&
                    selectedAnswer.equalsIgnoreCase(
                            question.getCorrectOption())) {
                score++;
            }
        }

        Result result = new Result();

        result.setStudentName(studentName);
        result.setRollNumber(rollNumber);
        result.setExamId(id);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        result.setSubmittedAt(LocalDateTime.now());

        Result savedResult = resultRepository.save(result);

        return "redirect:/result/" + savedResult.getId();
    }

    // Display examination result
    @GetMapping("/result/{id}")
    public String showResult(@PathVariable Long id,
                             HttpSession session,
                             Model model) {

        String rollNumber =
                (String) session.getAttribute("rollNumber");

        if (rollNumber == null) {
            return "redirect:/student-entry";
        }

        Result result = resultRepository.findById(id).orElse(null);

        if (result == null ||
                !rollNumber.equals(result.getRollNumber())) {
            return "redirect:/exams";
        }

        Exam exam = examRepository.findById(
                result.getExamId()).orElse(null);

        if (exam == null) {
            return "redirect:/exams";
        }

        model.addAttribute("result", result);
        model.addAttribute("exam", exam);

        double percentage = 0;

        if (result.getTotalQuestions() > 0) {
            percentage =
                    result.getScore() * 100.0 / result.getTotalQuestions();
        }

        model.addAttribute("percentage", percentage);

        return "result";
    }
}