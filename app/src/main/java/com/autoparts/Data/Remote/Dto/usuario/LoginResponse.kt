package com.autoparts.data.remote.dto.usuario

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("expiresIn")
    val expiresIn: Int,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("user")
    val user: UsuarioDto
)