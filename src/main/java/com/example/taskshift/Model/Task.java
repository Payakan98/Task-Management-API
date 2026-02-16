package com.example.taskshift.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant une tâche assignée à un employé
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties({"tasks", "shifts"})
    private Employee employee;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == TaskStatus.COMPLETED && completedDate == null) {
            completedDate = LocalDateTime.now();
        }
    }

    public boolean isOverdue() {
        return dueDate != null && 
               LocalDateTime.now().isAfter(dueDate) && 
               status != TaskStatus.COMPLETED;
    }

    public enum TaskStatus {
        TODO,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum TaskPriority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }
}
