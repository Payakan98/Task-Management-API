package com.example.taskshift.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entité représentant un quart de travail (shift) assigné à un employé
 */
@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(length = 50)
    private String shiftType; // Ex: MORNING, AFTERNOON, NIGHT, WEEKEND

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ShiftStatus status = ShiftStatus.SCHEDULED;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"tasks", "shifts"})
    private Employee employee;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        validateShiftTimes();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        validateShiftTimes();
    }

    private void validateShiftTimes() {
        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("L'heure de fin ne peut pas être avant l'heure de début");
        }
    }

    public long getDurationInHours() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime).toHours();
        }
        return 0;
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startTime);
    }

    public boolean isPast() {
        return LocalDateTime.now().isAfter(endTime);
    }

    public enum ShiftStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }
}
