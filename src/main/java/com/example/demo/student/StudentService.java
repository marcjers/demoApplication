package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public List<Student> getStudents() {
        return studentDAO.findAll();
    }

    public String addStudent(Student student) {
        Optional<Student> studentByEmail = studentDAO.findStudentByEmail(student.getEmail());
            if(studentByEmail.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            studentDAO.save(student);
        return "Saved";
    }

    public String deleteStudent(Long studentId) {
        boolean exist = studentDAO.existsById(studentId);

            if(!exist){
                throw new IllegalStateException("student with id " + studentId + " does not exists");
            }
            studentDAO.deleteById(studentId);
        return "Deleted";
    }

    @Transactional
    public String updateStudent(Long studentId, String name, String email) {
        Student student = studentDAO.findById(studentId).orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exists"));

        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
            student.setEmail(email);
        }

        return "Updated";
    }
}
