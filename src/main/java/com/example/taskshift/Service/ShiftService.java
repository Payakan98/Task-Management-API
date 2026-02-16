package com.example.taskshift.Service;

import com.example.taskshift.Model.Shift;
import com.example.taskshift.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    public Shift createShift(Shift shift) {
        // Vérification des conflits
        return shiftRepository.save(shift);
    }
}
