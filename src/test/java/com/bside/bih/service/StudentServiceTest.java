package com.bside.bih.service;

import com.bside.bih.domain.Student;
import com.bside.bih.exception.StudentNotFoundException;
import com.bside.bih.repository.IStudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @Mock
    private IStudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setFirstName("Jorge");
        student.setFirstSurname("Sanchez");
        student.setSecondSurname("Lopez");
        student.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student.setEmail("jsanchez@gmail.com");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);
        assertNotNull(createdStudent);
        assertEquals("Jorge", createdStudent.getFirstName());
        assertEquals("jsanchez@gmail.com", createdStudent.getEmail());
        assertEquals("1998-08-17", createdStudent.getDateOfBirth().toString());
        assertEquals("Sanchez", createdStudent.getFirstSurname());
        assertEquals("Lopez", createdStudent.getSecondSurname());
    }

    @Test
    public void testUpdateStudent() {

        Student existingStudent = new Student();
        existingStudent.setIdStudent(1L);
        existingStudent.setFirstName("Jorge");
        existingStudent.setFirstSurname("Sanchez");
        existingStudent.setSecondSurname("Lopez");
        existingStudent.setDateOfBirth(LocalDate.of(1998, 8, 17));
        existingStudent.setEmail("jsanchez@gmail.com");

        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("Jorge 2");
        updatedStudent.setFirstSurname("Sanchez2");
        updatedStudent.setSecondSurname("Lopez2");
        updatedStudent.setDateOfBirth(LocalDate.of(1998, 8, 17));
        updatedStudent.setEmail("jsanchez_2@gmail.com");

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Student resultStudent = studentService.updateStudent(1L, updatedStudent);

        assertNotNull(resultStudent);
        assertEquals("Jorge 2", resultStudent.getFirstName());
        assertEquals("jsanchez_2@gmail.com", resultStudent.getEmail());
        assertEquals("1998-08-17", resultStudent.getDateOfBirth().toString());
        assertEquals("Sanchez2", resultStudent.getFirstSurname());
        assertEquals("Lopez2", resultStudent.getSecondSurname());
    }

    @Test
    public void testFindAllStudents() {

        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("Jorge");
        student1.setFirstSurname("Sanchez");
        student1.setSecondSurname("Lopez");
        student1.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student1.setEmail("jsanchez@gmail.com");

        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Ana");
        student2.setFirstSurname("Flores");
        student2.setSecondSurname("Jimenez");
        student2.setDateOfBirth(LocalDate.of(1999, 8, 17));
        student2.setEmail("aflores@gmail.com");

        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> resultStudents = studentService.getAllStudents();

        assertNotNull(resultStudents);
        assertEquals(2, resultStudents.size());
        assertEquals("Ana", resultStudents.get(0).getFirstName());
        assertEquals("Jorge", resultStudents.get(1).getFirstName());
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Jorge");
        student.setFirstSurname("Sanchez");
        student.setSecondSurname("Lopez");
        student.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student.setEmail("jsanchez@gmail.com");

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        Student foundStudent = studentService.getStudentById(1L);
        assertNotNull(foundStudent);
        assertEquals("Jorge", foundStudent.getFirstName());
        assertEquals("jsanchez@gmail.com", foundStudent.getEmail());
        assertEquals("1998-08-17", foundStudent.getDateOfBirth().toString());
        assertEquals("Sanchez", foundStudent.getFirstSurname());
        assertEquals("Lopez", foundStudent.getSecondSurname());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(1L);
        });
    }

    @Test
    public void testDeleteStudent() {

        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);

    }
}
