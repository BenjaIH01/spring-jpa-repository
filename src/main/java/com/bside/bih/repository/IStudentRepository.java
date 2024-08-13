package com.bside.bih.repository;

import com.bside.bih.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> { }
