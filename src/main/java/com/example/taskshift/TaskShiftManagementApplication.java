package com.example.taskshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Task Shift Management
 * 
 * Cette application permet de gérer :
 * - Les employés (CRUD)
 * - Les tâches assignées aux employés
 * - Les quarts de travail (shifts) des employés
 */
@SpringBootApplication
public class TaskShiftManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskShiftManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("Task Shift Management API Started!");
        System.out.println("API Documentation: http://localhost:8080");
        System.out.println("========================================");
    }
}
