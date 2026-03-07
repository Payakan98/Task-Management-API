package com.example.service;

import com.example.taskshift.Model.Employee;
import com.example.taskshift.Model.Shift;
import com.example.taskshift.Repository.EmployeeRepository;
import com.example.taskshift.Repository.ShiftRepository;
import com.example.taskshift.Service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour ShiftService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ShiftService Tests")
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ShiftService shiftService;

    private Shift shift;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        shift = new Shift();
        shift.setId(1L);
        shift.setStartTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        shift.setEndTime(LocalDateTime.now().plusDays(1).withHour(17).withMinute(0));
        shift.setShiftType("MORNING");
        shift.setStatus(Shift.ShiftStatus.SCHEDULED);
        shift.setEmployee(employee);
    }

    @Test
    @DisplayName("Should create shift successfully without conflicts")
    void shouldCreateShiftSuccessfullyWithoutConflicts() {
        // Given
        when(shiftRepository.findConflictingShifts(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.createShift(shift);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getShiftType()).isEqualTo("MORNING");
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    @DisplayName("Should throw exception when creating shift with conflicts")
    void shouldThrowExceptionWhenCreatingShiftWithConflicts() {
        // Given
        Shift conflictingShift = new Shift();
        when(shiftRepository.findConflictingShifts(any(), any(), any()))
                .thenReturn(Arrays.asList(conflictingShift));

        // When & Then
        assertThatThrownBy(() -> shiftService.createShift(shift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Conflit détecté");
        
        verify(shiftRepository, never()).save(any(Shift.class));
    }

    @Test
    @DisplayName("Should throw exception when end time is before start time")
    void shouldThrowExceptionWhenEndTimeIsBeforeStartTime() {
        // Given
        shift.setStartTime(LocalDateTime.now().plusDays(1).withHour(17).withMinute(0));
        shift.setEndTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));

        // When & Then
        assertThatThrownBy(() -> shiftService.createShift(shift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("L'heure de fin ne peut pas être avant l'heure de début");
    }

    @Test
    @DisplayName("Should create shift for employee")
    void shouldCreateShiftForEmployee() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(shiftRepository.findConflictingShifts(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.createShiftForEmployee(shift, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmployee()).isEqualTo(employee);
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    @DisplayName("Should get active shifts")
    void shouldGetActiveShifts() {
        // Given
        Shift activeShift = new Shift();
        activeShift.setStartTime(LocalDateTime.now().minusHours(1));
        activeShift.setEndTime(LocalDateTime.now().plusHours(1));
        
        when(shiftRepository.findActiveShifts(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(activeShift));

        // When
        List<Shift> result = shiftService.getActiveShifts();

        // Then
        assertThat(result).hasSize(1);
        verify(shiftRepository, times(1)).findActiveShifts(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get upcoming shifts")
    void shouldGetUpcomingShifts() {
        // Given
        when(shiftRepository.findUpcomingShifts(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(shift));

        // When
        List<Shift> result = shiftService.getUpcomingShifts();

        // Then
        assertThat(result).hasSize(1);
        verify(shiftRepository, times(1)).findUpcomingShifts(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should change shift status")
    void shouldChangeShiftStatus() {
        // Given
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.changeShiftStatus(1L, Shift.ShiftStatus.IN_PROGRESS);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Shift.ShiftStatus.IN_PROGRESS);
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    @DisplayName("Should assign shift to employee")
    void shouldAssignShiftToEmployee() {
        // Given
        Employee newEmployee = new Employee();
        newEmployee.setId(2L);
        
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(newEmployee));
        when(shiftRepository.findConflictingShifts(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.assignShiftToEmployee(1L, 2L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmployee()).isEqualTo(newEmployee);
        verify(shiftRepository, times(1)).save(shift);
    }

    @Test
    @DisplayName("Should delete shift")
    void shouldDeleteShift() {
        // Given
        when(shiftRepository.existsById(1L)).thenReturn(true);
        doNothing().when(shiftRepository).deleteById(1L);

        // When
        shiftService.deleteShift(1L);

        // Then
        verify(shiftRepository, times(1)).deleteById(1L);
    }
}

