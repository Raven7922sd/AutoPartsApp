package com.autoparts.presentation.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null,
    val userId: String? = null
)
