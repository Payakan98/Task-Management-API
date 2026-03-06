package com.example.taskshift.Repository;

import com.example.taskshift.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Task
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Récupère toutes les tâches d'un employé
     */
    List<Task> findByEmployeeId(Long employeeId);

    /**
     * Récupère les tâches par statut
     */
    List<Task> findByStatus(Task.TaskStatus status);

    /**
     * Récupère les tâches par priorité
     */
    List<Task> findByPriority(Task.TaskPriority priority);

    /**
     * Récupère les tâches d'un employé par statut
     */
    List<Task> findByEmployeeIdAndStatus(Long employeeId, Task.TaskStatus status);

    /**
     * Récupère les tâches en retard (overdue)
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);

    /**
     * Récupère les tâches qui arrivent à échéance bientôt
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end AND t.status != 'COMPLETED'")
    List<Task> findTasksDueSoon(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Récupère les tâches d'un employé triées par priorité et date d'échéance
     */
    @Query("SELECT t FROM Task t WHERE t.employee.id = :employeeId " +
           "ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findByEmployeeIdOrderByPriorityAndDueDate(@Param("employeeId") Long employeeId);

    /**
     * Compte le nombre de tâches par statut pour un employé
     */
    long countByEmployeeIdAndStatus(Long employeeId, Task.TaskStatus status);

    /**
     * Recherche des tâches par mot-clé dans le titre ou la description
     */
    @Query("SELECT t FROM Task t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Compte toutes les tâches par statut
     */
    long countByStatus(Task.TaskStatus status);
}

