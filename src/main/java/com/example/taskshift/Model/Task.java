package com.example.taskshift.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;

    @ManyToOne
    private Employee assignedTo;

    // Getters et setters
}
