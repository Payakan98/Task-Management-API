package com.example.taskshift.Controller;

import com.example.taskshift.Model.Task;
import com.example.taskshift.Model.Task.TaskPriority;
import com.example.taskshift.Model.Task.TaskStatus;
import com.example.taskshift.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des tâches
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /api/tasks - Récupère toutes les tâches
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{id} - Récupère une tâche par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/tasks - Crée une nouvelle tâche
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * POST /api/tasks/employee/{employeeId} - Crée une tâche pour un employé
     */
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<Task> createTaskForEmployee(
            @Valid @RequestBody Task task,
            @PathVariable Long employeeId) {
        try {
            Task createdTask = taskService.createTaskForEmployee(task, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/tasks/{id} - Met à jour une tâche
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/tasks/{id} - Supprime une tâche
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/tasks/employee/{employeeId} - Récupère les tâches d'un employé
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Task>> getTasksByEmployee(@PathVariable Long employeeId) {
        List<Task> tasks = taskService.getTasksByEmployeeId(employeeId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/status/{status} - Récupère les tâches par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/priority/{priority} - Récupère les tâches par priorité
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable TaskPriority priority) {
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/employee/{employeeId}/status/{status} - Récupère les tâches d'un employé par statut
     */
    @GetMapping("/employee/{employeeId}/status/{status}")
    public ResponseEntity<List<Task>> getTasksByEmployeeAndStatus(
            @PathVariable Long employeeId,
            @PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByEmployeeAndStatus(employeeId, status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * PATCH /api/tasks/{id}/status - Change le statut d'une tâche
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> changeTaskStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            TaskStatus newStatus = TaskStatus.valueOf(statusUpdate.get("status"));
            Task updatedTask = taskService.changeTaskStatus(id, newStatus);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PATCH /api/tasks/{taskId}/assign/{employeeId} - Assigne une tâche à un employé
     */
    @PatchMapping("/{taskId}/assign/{employeeId}")
    public ResponseEntity<Task> assignTaskToEmployee(
            @PathVariable Long taskId,
            @PathVariable Long employeeId) {
        try {
            Task updatedTask = taskService.assignTaskToEmployee(taskId, employeeId);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/tasks/overdue - Récupère les tâches en retard
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        List<Task> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/due-soon?days={days} - Récupère les tâches qui arrivent à échéance bientôt
     */
    @GetMapping("/due-soon")
    public ResponseEntity<List<Task>> getTasksDueSoon(@RequestParam(defaultValue = "7") int days) {
        List<Task> tasks = taskService.getTasksDueSoon(days);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/employee/{employeeId}/sorted - Récupère les tâches d'un employé triées par priorité
     */
    @GetMapping("/employee/{employeeId}/sorted")
    public ResponseEntity<List<Task>> getTasksByEmployeeSorted(@PathVariable Long employeeId) {
        List<Task> tasks = taskService.getTasksByEmployeeOrderByPriority(employeeId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/search?keyword={keyword} - Recherche des tâches par mot-clé
     */
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/employee/{employeeId}/count/status/{status} - Compte les tâches d'un employé par statut
     */
    @GetMapping("/employee/{employeeId}/count/status/{status}")
    public ResponseEntity<Long> countTasksByEmployeeAndStatus(
            @PathVariable Long employeeId,
            @PathVariable TaskStatus status) {
        long count = taskService.countTasksByEmployeeAndStatus(employeeId, status);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/tasks/count/status/{status} - Compte toutes les tâches par statut
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countTasksByStatus(@PathVariable TaskStatus status) {
        long count = taskService.countTasksByStatus(status);
        return ResponseEntity.ok(count);
    }
}
