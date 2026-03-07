package com.example.service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Service.EmployeeService;
import com.example.taskshift.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour EmployeeService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPosition("Developer");
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should get all employees")
    void shouldGetAllEmployees() {
        // Given
        List<Employee> employees = Arrays.asList(employee, new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<Employee> result = employeeService.getAllEmployees();

        // Then
        assertThat(result).hasSize(2);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get employee by id")
    void shouldGetEmployeeById() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // When
        Optional<Employee> result = employeeService.getEmployeeById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create employee successfully")
    void shouldCreateEmployeeSuccessfully() {
        // Given
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.createEmployee(employee);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("Should throw exception when creating employee with existing email")
    void shouldThrowExceptionWhenCreatingEmployeeWithExistingEmail() {
        // Given
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> employeeService.createEmployee(employee))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email existe déjà");
        
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should update employee successfully")
    void shouldUpdateEmployeeSuccessfully() {
        // Given
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setPosition("Senior Developer");
        updatedEmployee.setStatus(Employee.EmployeeStatus.ACTIVE);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        // Then
        assertThat(result).isNotNull();
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void shouldDeleteEmployeeSuccessfully() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent employee")
    void shouldThrowExceptionWhenDeletingNonExistentEmployee() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non trouvé");
        
        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should search employees by name")
    void shouldSearchEmployeesByName() {
        // Given
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.searchByName("John")).thenReturn(employees);

        // When
        List<Employee> result = employeeService.searchEmployeesByName("John");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).searchByName("John");
    }

    @Test
    @DisplayName("Should count active employees")
    void shouldCountActiveEmployees() {
        // Given
        when(employeeRepository.countByStatus(Employee.EmployeeStatus.ACTIVE)).thenReturn(5L);

        // When
        long count = employeeService.countActiveEmployees();

        // Then
        assertThat(count).isEqualTo(5L);
        verify(employeeRepository, times(1)).countByStatus(Employee.EmployeeStatus.ACTIVE);
    }
}
