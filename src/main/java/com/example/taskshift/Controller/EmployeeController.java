package com.example.taskshift.Controller;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des employés
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * GET /api/employees - Récupère tous les employés
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/{id} - Récupère un employé par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/employees/email/{email} - Récupère un employé par email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/employees - Crée un nouvel employé
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/employees/{id} - Met à jour un employé
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/employees/{id} - Supprime un employé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/employees/status/{status} - Récupère les employés par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Employee>> getEmployeesByStatus(@PathVariable Employee.EmployeeStatus status) {
        List<Employee> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/position/{position} - Récupère les employés par poste
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        List<Employee> employees = employeeService.getEmployeesByPosition(position);
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/search?keyword={keyword} - Recherche des employés par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String keyword) {
        List<Employee> employees = employeeService.searchEmployeesByName(keyword);
        return ResponseEntity.ok(employees);
    }

    /**
     * PATCH /api/employees/{id}/status - Change le statut d'un employé
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Employee> changeEmployeeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            Employee.EmployeeStatus newStatus = Employee.EmployeeStatus.valueOf(statusUpdate.get("status"));
            Employee updatedEmployee = employeeService.changeEmployeeStatus(id, newStatus);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/employees/sorted - Récupère tous les employés triés par nom
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<Employee>> getAllEmployeesSorted() {
        List<Employee> employees = employeeService.getAllEmployeesSortedByName();
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /api/employees/count/active - Compte les employés actifs
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveEmployees() {
        long count = employeeService.countActiveEmployees();
        return ResponseEntity.ok(count);
    }
}