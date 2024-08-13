package com.bside.bih.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "student")
public class Student implements Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStudent;
    
    @NotEmpty(message = "Student's first name cannot be empty.")
    private String firstName;
    
    private String secondName;
    
    @NotEmpty(message = "Student's first surname cannot be empty.")
    private String firstSurname;
    
    @NotEmpty(message = "Student's second surname cannot be empty.")
    private String secondSurname;
    
    @NotNull(message = "The student's date of birth cannot be null.")
    private LocalDate dateOfBirth;
    
    @NotEmpty(message = "User's email cannot be empty.")
    @Email(message = "The email is not in the correct format.")
    private String email;

}
