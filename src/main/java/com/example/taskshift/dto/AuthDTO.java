package com.example.taskshift.dto;

import com.example.taskshift.Model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTOs pour l'authentification et l'enregistrement
 */
public class AuthDTO {

    /**
     * DTO pour la requête de connexion
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        private String username;

        @NotBlank(message = "Le mot de passe est obligatoire")
        private String password;
    }

    /**
     * DTO pour la requête d'enregistrement
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit être valide")
        private String email;

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
        private String password;

        private User.Role role = User.Role.EMPLOYEE;
    }

    /**
     * DTO pour la réponse d'authentification
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private User.Role role;

        public AuthResponse(String token, Long id, String username, String email, User.Role role) {
            this.token = token;
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }
    }

    /**
     * DTO pour les messages de réponse
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}

