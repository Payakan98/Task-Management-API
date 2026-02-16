package com.example.taskshift.Repository;

import com.example.taskshift.Model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ShiftRepository extends JpaRepository<Shift, Long> {}