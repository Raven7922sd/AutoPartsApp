package com.autoparts.data.remote.api

import com.autoparts.data.remote.dto.usuario.LoginRequest
import com.autoparts.data.remote.dto.usuario.LoginResponse
import com.autoparts.data.remote.dto.usuario.RegisterRequest
import com.autoparts.data.remote.dto.usuario.UpdateUsuarioRequest
import com.autoparts.data.remote.dto.usuario.UsuarioDto
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApiService {
    @POST("api/Users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/Users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<UsuarioDto>

    @GET("api/Users/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email") email: String): Response<UsuarioDto>

    @PUT("api/Users/{id}")
    suspend fun updateUsuario(
        @Path("id") id: String,
        @Body updateRequest: UpdateUsuarioRequest
    ): Response<Unit>
}