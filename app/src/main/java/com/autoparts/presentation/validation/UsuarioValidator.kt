package com.autoparts.presentation.validation

import javax.inject.Inject

class UsuarioValidator @Inject constructor() {

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El email es requerido"
            )
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!email.matches(emailRegex)) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El email no es válido"
            )
        }

        return ValidationResult(isValid = true, errorMessage = "")
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña necesita tener al menos 8 caracteres"
            )
        }

        if (!password.contains(Regex("[A-Z]"))) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos una letra mayúscula"
            )
        }

        if (!password.contains(Regex("[a-z]"))) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos una letra minúscula"
            )
        }

        if (!password.contains(Regex("[0-9]"))) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos un número"
            )
        }

        if (!password.contains(Regex("[^A-Za-z0-9]"))) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos un carácter especial"
            )
        }

        if (password.length > 50) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La contraseña no puede tener más de 50 caracteres"
            )
        }

        return ValidationResult(isValid = true, errorMessage = "")
    }

    fun validatePhoneNumber(phoneNumber: String?): ValidationResult {
        if (phoneNumber.isNullOrBlank()) {
            return ValidationResult(isValid = true, errorMessage = "")
        }

        val phoneRegex = "^[0-9]{10,15}$".toRegex()
        if (!phoneNumber.matches(phoneRegex)) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El teléfono debe contener entre 10 y 15 dígitos"
            )
        }

        return ValidationResult(isValid = true, errorMessage = "")
    }

    fun validateUserName(userName: String): ValidationResult {
        if (userName.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre es requerido"
            )
        }

        if (userName.length < 2) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre debe tener al menos 2 caracteres"
            )
        }

        if (userName.length > 50) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede tener más de 50 caracteres"
            )
        }

        return ValidationResult(isValid = true, errorMessage = "")
    }
}