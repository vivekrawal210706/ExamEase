
package com.examease.config;

import com.examease.entity.Exam;
import com.examease.entity.Question;
import com.examease.repository.ExamRepository;
import com.examease.repository.QuestionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    public DataInitializer(ExamRepository examRepository,
                           QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) {

        // Avoid inserting duplicate sample data
        if (examRepository.count() > 0) {
            return;
        }

        // Create first exam
        Exam javaExam = new Exam();
        javaExam.setTitle("Java Programming");
        javaExam.setDescription("Test your Java fundamentals.");
        javaExam.setDurationMinutes(15);
        examRepository.save(javaExam);

        // Create second exam
        Exam dbExam = new Exam();
        dbExam.setTitle("Database Management");
        dbExam.setDescription("Test your database knowledge.");
        dbExam.setDurationMinutes(15);
        examRepository.save(dbExam);

        // Create third exam
        Exam networkExam = new Exam();
        networkExam.setTitle("Computer Networks");
        networkExam.setDescription("Test your networking concepts.");
        networkExam.setDurationMinutes(15);
        examRepository.save(networkExam);

        // Java questions
        addQuestion(javaExam.getId(),
                "Which keyword is used to inherit a class in Java?",
                "implements", "extends", "inherits", "super", "B");

        addQuestion(javaExam.getId(),
                "Which method is the entry point of a Java program?",
                "start()", "run()", "main()", "execute()", "C");

        addQuestion(javaExam.getId(),
                "Which data type stores true or false?",
                "int", "char", "boolean", "float", "C");

        addQuestion(javaExam.getId(),
                "Which keyword creates an object in Java?",
                "class", "new", "this", "static", "B");

        addQuestion(javaExam.getId(),
                "Which symbol ends a Java statement?",
                ":", ".", ";", ",", "C");

        // Database questions
        addQuestion(dbExam.getId(),
                "What does DBMS stand for?",
                "Data Backup Management System",
                "Database Management System",
                "Digital Base Management System",
                "Data Building Management System", "B");

        addQuestion(dbExam.getId(),
                "Which language is commonly used to query databases?",
                "HTML", "CSS", "SQL", "JavaScript", "C");

        addQuestion(dbExam.getId(),
                "Which key uniquely identifies a table record?",
                "Foreign Key", "Primary Key",
                "Candidate Key", "Duplicate Key", "B");

        addQuestion(dbExam.getId(),
                "Which SQL command retrieves data?",
                "INSERT", "UPDATE", "SELECT", "DELETE", "C");

        addQuestion(dbExam.getId(),
                "What does SQL stand for?",
                "Structured Query Language",
                "Simple Question Language",
                "Standard Quick Language",
                "System Query Logic", "A");

        // Computer Networks questions
        addQuestion(networkExam.getId(),
                "What does LAN stand for?",
                "Local Area Network",
                "Large Area Network",
                "Long Access Network",
                "Linked Area Network", "A");

        addQuestion(networkExam.getId(),
                "Which device connects different networks?",
                "Switch", "Router", "Hub", "Repeater", "B");

        addQuestion(networkExam.getId(),
                "Which protocol is used to browse websites?",
                "FTP", "HTTP", "SMTP", "POP3", "B");

        addQuestion(networkExam.getId(),
                "How many layers are in the OSI model?",
                "4", "5", "7", "8", "C");

        addQuestion(networkExam.getId(),
                "Which address identifies a device on a network?",
                "IP Address", "Email Address",
                "Website Address", "File Address", "A");

        System.out.println("Sample examinations and questions inserted!");
    }

    private void addQuestion(Long examId, String text,
                             String a, String b, String c, String d,
                             String correct) {

        Question question = new Question();

        question.setExamId(examId);
        question.setQuestionText(text);
        question.setOptionA(a);
        question.setOptionB(b);
        question.setOptionC(c);
        question.setOptionD(d);
        question.setCorrectOption(correct);

        questionRepository.save(question);
    }
}

