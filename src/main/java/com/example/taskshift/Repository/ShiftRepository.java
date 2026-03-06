package com.example.taskshift.Repository;

import com.example.taskshift.Model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Shift
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    /**
     * Récupère tous les shifts d'un employé
     */
    List<Shift> findByEmployeeId(Long employeeId);

    /**
     * Récupère les shifts par statut
     */
    List<Shift> findByStatus(Shift.ShiftStatus status);

    /**
     * Récupère les shifts d'un employé par statut
     */
    List<Shift> findByEmployeeIdAndStatus(Long employeeId, Shift.ShiftStatus status);

    /**
     * Récupère les shifts dans une période donnée
     */
    @Query("SELECT s FROM Shift s WHERE s.startTime >= :start AND s.endTime <= :end")
    List<Shift> findShiftsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Récupère les shifts d'un employé dans une période donnée
     */
    @Query("SELECT s FROM Shift s WHERE s.employee.id = :employeeId " +
           "AND s.startTime >= :start AND s.endTime <= :end")
    List<Shift> findByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
                                               @Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    /**
     * Récupère les shifts actifs (en cours)
     */
    @Query("SELECT s FROM Shift s WHERE s.startTime <= :now AND s.endTime >= :now")
    List<Shift> findActiveShifts(@Param("now") LocalDateTime now);

    /**
     * Récupère les shifts à venir
     */
    @Query("SELECT s FROM Shift s WHERE s.startTime > :now ORDER BY s.startTime ASC")
    List<Shift> findUpcomingShifts(@Param("now") LocalDateTime now);

    /**
     * Récupère les shifts d'un employé triés par date de début
     */
    List<Shift> findByEmployeeIdOrderByStartTimeDesc(Long employeeId);

    /**
     * Vérifie s'il y a un conflit de shift pour un employé
     */
    @Query("SELECT s FROM Shift s WHERE s.employee.id = :employeeId " +
           "AND ((s.startTime <= :endTime AND s.endTime >= :startTime)) " +
           "AND s.status != 'CANCELLED'")
    List<Shift> findConflictingShifts(@Param("employeeId") Long employeeId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * Récupère les shifts par type
     */
    List<Shift> findByShiftType(String shiftType);

    /**
     * Compte le nombre de shifts d'un employé dans une période
     */
    @Query("SELECT COUNT(s) FROM Shift s WHERE s.employee.id = :employeeId " +
           "AND s.startTime >= :start AND s.endTime <= :end")
    long countByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
                                         @Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    /**
     * Compte les shifts par statut
     */
    long countByStatus(Shift.ShiftStatus status);
}
