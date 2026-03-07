package com.example.service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Model.Task;
import com.example.taskshift.Repository.EmployeeRepository;
import com.example.taskshift.Service.TaskService;
import com.example.taskshift.Repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour TaskService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(Task.TaskStatus.TODO);
        task.setPriority(Task.TaskPriority.MEDIUM);
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setEmployee(employee);
    }

    @Test
    @DisplayName("Should get all tasks")
    void shouldGetAllTasks() {
        // Given
        List<Task> tasks = Arrays.asList(task, new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertThat(result).hasSize(2);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create task for employee")
    void shouldCreateTaskForEmployee() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.createTaskForEmployee(task, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmployee()).isEqualTo(employee);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should throw exception when creating task for non-existent employee")
    void shouldThrowExceptionWhenCreatingTaskForNonExistentEmployee() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.createTaskForEmployee(task, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Employé non trouvé");
        
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should assign task to employee")
    void shouldAssignTaskToEmployee() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.assignTaskToEmployee(1L, 2L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmployee()).isEqualTo(employee);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should change task status")
    void shouldChangeTaskStatus() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.changeTaskStatus(1L, Task.TaskStatus.IN_PROGRESS);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Task.TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should get overdue tasks")
    void shouldGetOverdueTasks() {
        // Given
        Task overdueTask = new Task();
        overdueTask.setDueDate(LocalDateTime.now().minusDays(1));
        overdueTask.setStatus(Task.TaskStatus.TODO);
        
        when(taskRepository.findOverdueTasks(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(overdueTask));

        // When
        List<Task> result = taskService.getOverdueTasks();

        // Then
        assertThat(result).hasSize(1);
        verify(taskRepository, times(1)).findOverdueTasks(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get tasks by employee")
    void shouldGetTasksByEmployee() {
        // Given
        when(taskRepository.findByEmployeeId(1L)).thenReturn(Arrays.asList(task));

        // When
        List<Task> result = taskService.getTasksByEmployeeId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployee().getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).findByEmployeeId(1L);
    }

    @Test
    @DisplayName("Should delete task")
    void shouldDeleteTask() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).deleteById(1L);
    }
}

