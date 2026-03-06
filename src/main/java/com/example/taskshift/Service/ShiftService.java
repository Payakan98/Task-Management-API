package com.example.taskshift.Service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Model.Shift;
import com.example.taskshift.Repository.EmployeeRepository;
import com.example.taskshift.Repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des quarts de travail (shifts)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Récupère tous les shifts
     */
    @Transactional(readOnly = true)
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    /**
     * Récupère un shift par son ID
     */
    @Transactional(readOnly = true)
    public Optional<Shift> getShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    /**
     * Crée un nouveau shift
     */
    public Shift createShift(Shift shift) {
        // Validation des dates
        if (shift.getEndTime().isBefore(shift.getStartTime())) {
            throw new IllegalArgumentException("L'heure de fin ne peut pas être avant l'heure de début");
        }

        // Vérifier les conflits de shifts
        if (shift.getEmployee() != null) {
            List<Shift> conflicts = shiftRepository.findConflictingShifts(
                    shift.getEmployee().getId(),
                    shift.getStartTime(),
                    shift.getEndTime()
            );
            
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException(
                        "Conflit détecté : l'employé a déjà un shift programmé pendant cette période");
            }
        }

        return shiftRepository.save(shift);
    }

    /**
     * Crée un shift pour un employé
     */
    public Shift createShiftForEmployee(Shift shift, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + employeeId));

        shift.setEmployee(employee);

        // Vérifier les conflits de shifts
        List<Shift> conflicts = shiftRepository.findConflictingShifts(
                employeeId,
                shift.getStartTime(),
                shift.getEndTime()
        );
        
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException(
                    "Conflit détecté : l'employé a déjà un shift programmé pendant cette période");
        }

        return shiftRepository.save(shift);
    }

    /**
     * Met à jour un shift existant
     */
    public Shift updateShift(Long id, Shift shiftDetails) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shift non trouvé avec l'id : " + id));

        // Validation des dates
        if (shiftDetails.getEndTime().isBefore(shiftDetails.getStartTime())) {
            throw new IllegalArgumentException("L'heure de fin ne peut pas être avant l'heure de début");
        }

        // Vérifier les conflits si les dates ou l'employé changent
        if (shift.getEmployee() != null && 
            (!shift.getStartTime().equals(shiftDetails.getStartTime()) ||
             !shift.getEndTime().equals(shiftDetails.getEndTime()))) {
            
            List<Shift> conflicts = shiftRepository.findConflictingShifts(
                    shift.getEmployee().getId(),
                    shiftDetails.getStartTime(),
                    shiftDetails.getEndTime()
            );
            
            // Exclure le shift actuel des conflits
            conflicts.removeIf(s -> s.getId().equals(id));
            
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException(
                        "Conflit détecté : l'employé a déjà un shift programmé pendant cette période");
            }
        }

        shift.setStartTime(shiftDetails.getStartTime());
        shift.setEndTime(shiftDetails.getEndTime());
        shift.setShiftType(shiftDetails.getShiftType());
        shift.setStatus(shiftDetails.getStatus());
        shift.setNotes(shiftDetails.getNotes());

        return shiftRepository.save(shift);
    }

    /**
     * Supprime un shift
     */
    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new IllegalArgumentException("Shift non trouvé avec l'id : " + id);
        }
        shiftRepository.deleteById(id);
    }

    /**
     * Récupère les shifts d'un employé
     */
    @Transactional(readOnly = true)
    public List<Shift> getShiftsByEmployeeId(Long employeeId) {
        return shiftRepository.findByEmployeeId(employeeId);
    }

    /**
     * Récupère les shifts par statut
     */
    @Transactional(readOnly = true)
    public List<Shift> getShiftsByStatus(Shift.ShiftStatus status) {
        return shiftRepository.findByStatus(status);
    }

    /**
     * Récupère les shifts dans une période donnée
     */
    @Transactional(readOnly = true)
    public List<Shift> getShiftsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findShiftsBetween(start, end);
    }

    /**
     * Récupère les shifts d'un employé dans une période donnée
     */
    @Transactional(readOnly = true)
    public List<Shift> getShiftsByEmployeeAndDateRange(Long employeeId, LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findByEmployeeIdAndDateRange(employeeId, start, end);
    }

    /**
     * Récupère les shifts actifs (en cours)
     */
    @Transactional(readOnly = true)
    public List<Shift> getActiveShifts() {
        return shiftRepository.findActiveShifts(LocalDateTime.now());
    }

    /**
     * Récupère les shifts à venir
     */
    @Transactional(readOnly = true)
    public List<Shift> getUpcomingShifts() {
        return shiftRepository.findUpcomingShifts(LocalDateTime.now());
    }

    /**
     * Change le statut d'un shift
     */
    public Shift changeShiftStatus(Long id, Shift.ShiftStatus newStatus) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shift non trouvé avec l'id : " + id));
        
        shift.setStatus(newStatus);
        return shiftRepository.save(shift);
    }

    /**
     * Assigne un shift à un employé
     */
    public Shift assignShiftToEmployee(Long shiftId, Long employeeId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift non trouvé avec l'id : " + shiftId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec l'id : " + employeeId));

        // Vérifier les conflits
        List<Shift> conflicts = shiftRepository.findConflictingShifts(
                employeeId,
                shift.getStartTime(),
                shift.getEndTime()
        );
        
        // Exclure le shift actuel des conflits
        conflicts.removeIf(s -> s.getId().equals(shiftId));
        
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException(
                    "Conflit détecté : l'employé a déjà un shift programmé pendant cette période");
        }
        
        shift.setEmployee(employee);
        return shiftRepository.save(shift);
    }

    /**
     * Récupère les shifts par type
     */
    @Transactional(readOnly = true)
    public List<Shift> getShiftsByType(String shiftType) {
        return shiftRepository.findByShiftType(shiftType);
    }

    /**
     * Compte les shifts d'un employé dans une période
     */
    @Transactional(readOnly = true)
    public long countShiftsByEmployeeAndDateRange(Long employeeId, LocalDateTime start, LocalDateTime end) {
        return shiftRepository.countByEmployeeIdAndDateRange(employeeId, start, end);
    }

    /**
     * Compte les shifts par statut
     */
    @Transactional(readOnly = true)
    public long countShiftsByStatus(Shift.ShiftStatus status) {
        return shiftRepository.countByStatus(status);
    }
}

