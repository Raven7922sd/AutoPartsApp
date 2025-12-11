package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.UsuariosApiService
import com.autoparts.data.remote.dto.usuario.LoginRequest
import com.autoparts.data.remote.dto.usuario.LoginResult
import com.autoparts.data.remote.dto.usuario.RegisterRequest
import com.autoparts.data.remote.dto.usuario.UpdateUsuarioRequest
import com.autoparts.data.remote.dto.usuario.UsuarioDto
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: UsuariosApiService
) {
    companion object {
        private const val ERROR_CONEXION = "Error de conexión"
    }

    suspend fun login(loginRequest: LoginRequest): Resource<LoginResult> {
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val loginResult = LoginResult(
                    usuario = loginResponse.user,
                    accessToken = loginResponse.accessToken,
                    refreshToken = loginResponse.refreshToken,
                    expiresIn = loginResponse.expiresIn
                )
                Resource.Success(loginResult)
            } else {
                Resource.Error(message = "Email o contraseña incorrectos")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_CONEXION)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Resource<UsuarioDto> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al registrar usuario")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_CONEXION)
        }
    }

    suspend fun getUsuarioByEmail(email: String): Resource<UsuarioDto> {
        return try {
            val response = apiService.getUsuarioByEmail(email)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Usuario no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_CONEXION)
        }
    }

    suspend fun updateUsuario(id: String, updateRequest: UpdateUsuarioRequest): Resource<Unit> {
        return try {
            val response = apiService.updateUsuario(id, updateRequest)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Error al actualizar usuario")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_CONEXION)
        }
    }
}