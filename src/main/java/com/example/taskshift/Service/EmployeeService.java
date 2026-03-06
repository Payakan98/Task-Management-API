package com.example.taskshift.Service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Model.Employee.EmployeeStatus;
import com.example.taskshift.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des employés
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Récupère tous les employés
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Récupère un employé par son ID
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * Récupère un employé par son email
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    /**
     * Crée un nouvel employé
     */
    public Employee createEmployee(Employee employee) {
        // Vérifier si l'email existe déjà
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Un employé avec cet email existe déjà");
        }
        return employeeRepository.save(employee);
    }

    /**
     * Met à jour un employé existant
     */
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + id));

        // Vérifier si le nouvel email est déjà utilisé par un autre employé
        if (!employee.getEmail().equals(employeeDetails.getEmail()) &&
                employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new IllegalArgumentException("Un employé avec cet email existe déjà");
        }

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setPosition(employeeDetails.getPosition());
        employee.setStatus(employeeDetails.getStatus());

        return employeeRepository.save(employee);
    }

    /**
     * Supprime un employé
     */
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employé non trouvé avec l'id : " + id);
        }
        employeeRepository.deleteById(id);
    }

    /**
     * Récupère les employés par statut
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }

    /**
     * Récupère les employés par poste
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }

    /**
     * Recherche des employés par nom
     */
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByName(String keyword) {
        return employeeRepository.searchByName(keyword);
    }

    /**
     * Change le statut d'un employé
     */
    public Employee changeEmployeeStatus(Long id, EmployeeStatus newStatus) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + id));
        
        employee.setStatus(newStatus);
        return employeeRepository.save(employee);
    }

    /**
     * Compte le nombre d'employés actifs
     */
    @Transactional(readOnly = true)
    public long countActiveEmployees() {
        return employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
    }

    /**
     * Récupère tous les employés triés par nom
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployeesSortedByName() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }
}
