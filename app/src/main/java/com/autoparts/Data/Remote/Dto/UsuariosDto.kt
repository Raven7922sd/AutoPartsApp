package com.autoparts.Data.Remote.Dto


data class UserDto(
    val id: String,
    val userName: String?,
    val email: String,
    val phoneNumber: String?,
    val emailConfirmed: Boolean = false,
    val phoneNumberConfirmed: Boolean = false,
    val twoFactorEnabled: Boolean = false,
    val lockoutEnabled: Boolean = false,
    val lockoutEnd: String?,
    val accessFailedCount: Int = 0,
    val roles: List<String> = emptyList()
)

data class CreateUserDto(
    val email: String,
    val password: String,
    val phoneNumber: String? = null
)

data class LoginDto(
    val email: String,
    val password: String
)

data class UpdateUserDto(
    val email: String?,
    val phoneNumber: String?,
    val currentPassword: String?,
    val newPassword: String?
)

@Deprecated("Usar UserDto en su lugar")
data class UsuariosDto(
    val usuarioId: Int?,
    val userName: String,
    val password: String
)
