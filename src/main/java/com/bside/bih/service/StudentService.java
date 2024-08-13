package com.bside.bih.service;

import com.bside.bih.domain.Student;
import com.bside.bih.repository.IStudentRepository;
import com.bside.bih.exception.StudentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private IStudentRepository studentRepository;

    @Transactional(readOnly=true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll().stream()
                .filter(student -> student.getEmail() != null && !student.getEmail().isEmpty())
                .sorted((s1, s2) -> s1.getFirstSurname().compareTo(s2.getFirstSurname()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setSecondName(student.getSecondName());
                    existingStudent.setFirstSurname(student.getFirstSurname());
                    existingStudent.setSecondSurname(student.getSecondSurname());
                    existingStudent.setDateOfBirth(student.getDateOfBirth());
                    existingStudent.setEmail(student.getEmail());
                    return studentRepository.save(existingStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }
}
