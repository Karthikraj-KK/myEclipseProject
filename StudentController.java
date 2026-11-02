package com.example.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.model.Student;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")

public class StudentController {


    List<Student> students = new ArrayList<>();

    @PostMapping
    public String registerStudent(
            @Valid @RequestBody Student student) {
          students.add(student);
        return "Student registered successfully";
    }

    @GetMapping
    public List<Student> getAllStudents() {
         return students;
    }

   
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {

        for (Student student : students) {
             if (student.getStudentId() == id) {
             return student;
            }
        }
         return null;
    }

   
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {

        boolean removed = students.removeIf(
                student -> student.getStudentId() == id);

        if (removed) {
            return "Student registration deleted successfully";
        }

        return "Student not found";
    }
}
