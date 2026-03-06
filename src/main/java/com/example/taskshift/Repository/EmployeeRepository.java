package com.example.taskshift.Repository;

import com.example.taskshift.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Employee
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Recherche un employé par email
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Recherche des employés par statut
     */
    List<Employee> findByStatus(Employee.EmployeeStatus status);

    /**
     * Recherche des employés par poste
     */
    List<Employee> findByPosition(String position);

    /**
     * Recherche des employés par nom ou prénom (insensible à la casse)
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByName(@Param("keyword") String keyword);

    /**
     * Compte le nombre d'employés actifs
     */
    long countByStatus(Employee.EmployeeStatus status);

    /**
     * Récupère tous les employés triés par nom de famille
     */
    List<Employee> findAllByOrderByLastNameAsc();
}

