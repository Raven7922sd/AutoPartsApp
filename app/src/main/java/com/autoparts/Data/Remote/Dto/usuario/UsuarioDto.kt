package com.autoparts.data.remote.dto.usuario

import com.google.gson.annotations.SerializedName

data class UsuarioDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("emailConfirmed")
    val emailConfirmed: Boolean,
    @SerializedName("roles")
    val roles: List<String>
)