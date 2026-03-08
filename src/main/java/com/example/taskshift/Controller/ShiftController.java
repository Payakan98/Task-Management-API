package com.example.taskshift.Controller;

import com.example.taskshift.Model.Shift;
import com.example.taskshift.Service.ShiftService;
import com.example.taskshift.exception.ConflictException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.taskshift.exception.ConflictException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des quarts de travail (shifts)
 */
@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShiftController {

    private final ShiftService shiftService;

    /**
     * GET /api/shifts - Récupère tous les shifts
     */
    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/{id} - Récupère un shift par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Shift> getShiftById(@PathVariable Long id) {
        return shiftService.getShiftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/shifts - Crée un nouveau shift
     */
    @PostMapping
   
    public ResponseEntity<?> createShift(@Valid @RequestBody Shift shift) {
        try {
            Shift createdShift = shiftService.createShift(shift);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdShift);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/shifts/employee/{employeeId} - Crée un shift pour un employé
     */
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<?> createShiftForEmployee(
            @Valid @RequestBody Shift shift,
            @PathVariable Long employeeId) {
        try {
            Shift createdShift = shiftService.createShiftForEmployee(shift, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdShift);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/shifts/{id} - Met à jour un shift
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShift(
            @PathVariable Long id,
            @Valid @RequestBody Shift shift) {
        try {
            Shift updatedShift = shiftService.updateShift(id, shift);
            return ResponseEntity.ok(updatedShift);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/shifts/{id} - Supprime un shift
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        try {
            shiftService.deleteShift(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/shifts/employee/{employeeId} - Récupère les shifts d'un employé
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Shift>> getShiftsByEmployee(@PathVariable Long employeeId) {
        List<Shift> shifts = shiftService.getShiftsByEmployeeId(employeeId);
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/status/{status} - Récupère les shifts par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Shift>> getShiftsByStatus(@PathVariable Shift.ShiftStatus status) {
        List<Shift> shifts = shiftService.getShiftsByStatus(status);
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/between - Récupère les shifts dans une période donnée
     * Paramètres: start, end (format: yyyy-MM-dd'T'HH:mm:ss)
     */
    @GetMapping("/between")
    public ResponseEntity<List<Shift>> getShiftsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Shift> shifts = shiftService.getShiftsBetweenDates(start, end);
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/employee/{employeeId}/between - Récupère les shifts d'un employé dans une période
     */
    @GetMapping("/employee/{employeeId}/between")
    public ResponseEntity<List<Shift>> getShiftsByEmployeeAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Shift> shifts = shiftService.getShiftsByEmployeeAndDateRange(employeeId, start, end);
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/active - Récupère les shifts actifs (en cours)
     */
    @GetMapping("/active")
    public ResponseEntity<List<Shift>> getActiveShifts() {
        List<Shift> shifts = shiftService.getActiveShifts();
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/upcoming - Récupère les shifts à venir
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<Shift>> getUpcomingShifts() {
        List<Shift> shifts = shiftService.getUpcomingShifts();
        return ResponseEntity.ok(shifts);
    }

    /**
     * PATCH /api/shifts/{id}/status - Change le statut d'un shift
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Shift> changeShiftStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            Shift.ShiftStatus newStatus = Shift.ShiftStatus.valueOf(statusUpdate.get("status"));
            Shift updatedShift = shiftService.changeShiftStatus(id, newStatus);
            return ResponseEntity.ok(updatedShift);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PATCH /api/shifts/{shiftId}/assign/{employeeId} - Assigne un shift à un employé
     */
    @PatchMapping("/{shiftId}/assign/{employeeId}")
    public ResponseEntity<?> assignShiftToEmployee(
            @PathVariable Long shiftId,
            @PathVariable Long employeeId) {
        try {
            Shift updatedShift = shiftService.assignShiftToEmployee(shiftId, employeeId);
            return ResponseEntity.ok(updatedShift);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/shifts/type/{shiftType} - Récupère les shifts par type
     */
    @GetMapping("/type/{shiftType}")
    public ResponseEntity<List<Shift>> getShiftsByType(@PathVariable String shiftType) {
        List<Shift> shifts = shiftService.getShiftsByType(shiftType);
        return ResponseEntity.ok(shifts);
    }

    /**
     * GET /api/shifts/employee/{employeeId}/count/between - Compte les shifts d'un employé dans une période
     */
    @GetMapping("/employee/{employeeId}/count/between")
    public ResponseEntity<Long> countShiftsByEmployeeAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        long count = shiftService.countShiftsByEmployeeAndDateRange(employeeId, start, end);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/shifts/count/status/{status} - Compte les shifts par statut
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countShiftsByStatus(@PathVariable Shift.ShiftStatus status) {
        long count = shiftService.countShiftsByStatus(status);
        return ResponseEntity.ok(count);
    }
}
