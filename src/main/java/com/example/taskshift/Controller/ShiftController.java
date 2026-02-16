package com.example.taskshift.Controller;

import com.example.taskshift.Model.Shift;
import com.example.taskshift.Service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Shift")
public class ShiftController {
    @Autowired
    private ShiftService taskService;

    @PostMapping
    public Shift createTask(@RequestBody Shift shift) {
        return ShiftService.createShift(shift);
    }

    @GetMapping
    public List<Shift> getShifts() {
        return taskService.getAllShifts();
    }
}
