package com.bside.bih.controller;

import com.bside.bih.domain.Student;
import com.bside.bih.service.StudentService;
import com.bside.bih.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setFirstName("Jorge");
        student.setFirstSurname("Sanchez");
        student.setSecondSurname("Lopez");
        student.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student.setEmail("jsanchez@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/students/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jorge"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jsanchez@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Sanchez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondSurname").value("Lopez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1998-08-17"));
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("Jorge");
        student1.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student1.setEmail("jsanchez@gmail.com");

        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Ana");
        student2.setDateOfBirth(LocalDate.of(1999, 5, 12));
        student2.setEmail("ana@example.com");

        List<Student> students = List.of(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Jorge"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Ana"));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Jorge");
        student.setFirstSurname("Sanchez");
        student.setSecondSurname("Lopez");
        student.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student.setEmail("jsanchez@gmail.com");

        when(studentService.getStudentById(anyLong())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/search/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jorge"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jsanchez@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Sanchez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondSurname").value("Lopez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1998-08-17"));
    }

    @Test
    public void testGetStudentByIdNotFound() throws Exception {
        when(studentService.getStudentById(anyLong())).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/search/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteStudent() throws Exception {

        doNothing().when(studentService).deleteStudent(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Jorge");
        student.setFirstSurname("Sanchez");
        student.setSecondSurname("Lopez");
        student.setDateOfBirth(LocalDate.of(1998, 8, 17));
        student.setEmail("jsanchez@gmail.com");

        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("Jorge 2");
        updatedStudent.setFirstSurname("Sanchez2");
        updatedStudent.setSecondSurname("Lopez2");
        updatedStudent.setDateOfBirth(LocalDate.of(1998, 8, 17));
        updatedStudent.setEmail("jsanchez_2@gmail.com");

        when(studentService.updateStudent(anyLong(), any(Student.class))).thenReturn(updatedStudent);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.put("/students/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jorge 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jsanchez_2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstSurname").value("Sanchez2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondSurname").value("Lopez2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1998-08-17"));
    }

}
