package com.autoparts.Data.Mappers

import com.autoparts.Data.Remote.Dto.CreateUserDto
import com.autoparts.Data.Remote.Dto.LoginDto
import com.autoparts.Data.Remote.Dto.LoginResponse
import com.autoparts.Data.Remote.Dto.UpdateUserDto
import com.autoparts.Data.Remote.Dto.UserDto
import com.autoparts.dominio.model.CreateUser
import com.autoparts.dominio.model.LoginResult
import com.autoparts.dominio.model.LoginUser
import com.autoparts.dominio.model.UpdateUser
import com.autoparts.dominio.model.Usuarios

fun UserDto.toDomain(): Usuarios = Usuarios(
    id = id,
    userName = userName,
    email = email,
    phoneNumber = phoneNumber,
    emailConfirmed = emailConfirmed,
    roles = roles
)

fun Usuarios.toDto(): UserDto = UserDto(
    id = id,
    userName = userName,
    email = email,
    phoneNumber = phoneNumber,
    emailConfirmed = emailConfirmed,
    phoneNumberConfirmed = false,
    twoFactorEnabled = false,
    lockoutEnabled = false,
    lockoutEnd = null,
    accessFailedCount = 0,
    roles = roles
)

fun CreateUser.toDto(): CreateUserDto = CreateUserDto(
    email = email,
    password = password,
    phoneNumber = phoneNumber
)

fun LoginUser.toDto(): LoginDto = LoginDto(
    email = email,
    password = password
)

fun LoginResponse.toDomain(email: String): LoginResult = LoginResult(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    email = email
)

fun UpdateUser.toDto(): UpdateUserDto = UpdateUserDto(
    email = email,
    phoneNumber = phoneNumber,
    currentPassword = currentPassword,
    newPassword = newPassword
)