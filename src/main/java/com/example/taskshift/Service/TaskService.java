package com.example.taskshift.Service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Model.Task;
import com.example.taskshift.Repository.EmployeeRepository;
import com.example.taskshift.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des tâches
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Récupère toutes les tâches
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Récupère une tâche par son ID
     */
    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Crée une nouvelle tâche
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Crée une tâche et l'assigne à un employé
     */
    public Task createTaskForEmployee(Task task, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + employeeId));
        
        task.setEmployee(employee);
        return taskRepository.save(task);
    }

    /**
     * Met à jour une tâche existante
     */
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée avec l'id : " + id));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setDueDate(taskDetails.getDueDate());

        if (taskDetails.getEmployee() != null) {
            task.setEmployee(taskDetails.getEmployee());
        }

        return taskRepository.save(task);
    }

    /**
     * Supprime une tâche
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Tâche non trouvée avec l'id : " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Récupère les tâches d'un employé
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByEmployeeId(Long employeeId) {
        return taskRepository.findByEmployeeId(employeeId);
    }

    /**
     * Récupère les tâches par statut
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Récupère les tâches par priorité
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByPriority(Task.TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    /**
     * Récupère les tâches d'un employé par statut
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByEmployeeAndStatus(Long employeeId, Task.TaskStatus status) {
        return taskRepository.findByEmployeeIdAndStatus(employeeId, status);
    }

    /**
     * Change le statut d'une tâche
     */
    public Task changeTaskStatus(Long id, Task.TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée avec l'id : " + id));
        
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    /**
     * Assigne une tâche à un employé
     */
    public Task assignTaskToEmployee(Long taskId, Long employeeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée avec l'id : " + taskId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + employeeId));
        
        task.setEmployee(employee);
        return taskRepository.save(task);
    }

    /**
     * Récupère les tâches en retard
     */
    @Transactional(readOnly = true)
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }

    /**
     * Récupère les tâches qui arrivent à échéance bientôt (dans les prochains jours)
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksDueSoon(int days) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(days);
        return taskRepository.findTasksDueSoon(start, end);
    }

    /**
     * Récupère les tâches d'un employé triées par priorité et date d'échéance
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByEmployeeOrderByPriority(Long employeeId) {
        return taskRepository.findByEmployeeIdOrderByPriorityAndDueDate(employeeId);
    }

    /**
     * Recherche des tâches par mot-clé
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasks(String keyword) {
        return taskRepository.searchByKeyword(keyword);
    }

    /**
     * Compte les tâches par statut pour un employé
     */
    @Transactional(readOnly = true)
    public long countTasksByEmployeeAndStatus(Long employeeId, Task.TaskStatus status) {
        return taskRepository.countByEmployeeIdAndStatus(employeeId, status);
    }

    /**
     * Compte toutes les tâches par statut
     */
    @Transactional(readOnly = true)
    public long countTasksByStatus(Task.TaskStatus status) {
        return taskRepository.countByStatus(status);
    }
}
