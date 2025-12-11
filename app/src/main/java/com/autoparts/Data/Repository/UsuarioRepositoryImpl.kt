package com.autoparts.data.repository

import com.autoparts.data.local.manager.SessionManager
import com.autoparts.data.remote.datasource.UsuarioRemoteDataSource
import com.autoparts.data.remote.dto.usuario.LoginRequest
import com.autoparts.data.remote.dto.usuario.RegisterRequest
import com.autoparts.data.remote.dto.usuario.UpdateUsuarioRequest
import com.autoparts.data.remote.mapper.UsuarioMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateUser
import com.autoparts.domain.model.UpdateUser
import com.autoparts.domain.model.Usuarios
import com.autoparts.domain.repository.UsuarioRepository
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource,
    private val sessionManager: SessionManager
) : UsuarioRepository {

    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
    }

    override suspend fun register(createUser: CreateUser): Resource<Usuarios> {
        val request = RegisterRequest(
            email = createUser.email,
            password = createUser.password,
            phoneNumber = createUser.phoneNumber
        )

        return when (val result = remoteDataSource.register(request)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(UsuarioMapper.toDomain(dto))
                } ?: Resource.Error("Error al registrar usuario")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun login(email: String, password: String): Resource<Usuarios> {
        val request = LoginRequest(email, password)

        return when (val result = remoteDataSource.login(request)) {
            is Resource.Success -> {
                result.data?.let { loginResult ->
                    val dto = loginResult.usuario
                    val userId = dto.id ?: dto.email

                    sessionManager.saveUserEmail(dto.email)
                    sessionManager.saveUserId(userId)
                    sessionManager.saveToken(loginResult.accessToken)

                    Resource.Success(UsuarioMapper.toDomain(dto))
                } ?: Resource.Error("Error al iniciar sesión")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override suspend fun updateUsuario(id: String, updateUser: UpdateUser): Resource<Unit> {
        val request = UpdateUsuarioRequest(
            email = updateUser.email,
            phoneNumber = updateUser.phoneNumber,
            currentPassword = updateUser.currentPassword,
            newPassword = updateUser.newPassword
        )

        return remoteDataSource.updateUsuario(id, request)
    }

    override suspend fun getUsuarioByEmail(email: String): Resource<Usuarios> {
        return when (val result = remoteDataSource.getUsuarioByEmail(email)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(UsuarioMapper.toDomain(dto))
                } ?: Resource.Error("Usuario no encontrado")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }
}