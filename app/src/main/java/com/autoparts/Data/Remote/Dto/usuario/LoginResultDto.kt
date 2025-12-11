package com.autoparts.data.remote.dto.usuario

data class LoginResult(
    val usuario: UsuarioDto,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)