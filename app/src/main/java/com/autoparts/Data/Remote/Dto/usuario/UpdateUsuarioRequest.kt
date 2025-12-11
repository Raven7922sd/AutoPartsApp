package com.autoparts.data.remote.dto.usuario

import com.google.gson.annotations.SerializedName

data class UpdateUsuarioRequest(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("currentPassword")
    val currentPassword: String? = null,
    @SerializedName("newPassword")
    val newPassword: String? = null
)