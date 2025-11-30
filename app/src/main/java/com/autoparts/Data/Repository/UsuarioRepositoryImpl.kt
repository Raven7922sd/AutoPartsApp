package com.autoparts.Data.Repository

import com.autoparts.Data.Mappers.toDomain
import com.autoparts.Data.Mappers.toDto
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.Remote.UsuarioRemoteDataSource
import com.autoparts.dominio.model.CreateUser
import com.autoparts.dominio.model.LoginResult
import com.autoparts.dominio.model.LoginUser
import com.autoparts.dominio.model.UpdateUser
import com.autoparts.dominio.model.Usuarios
import com.autoparts.dominio.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val dataSource: UsuarioRemoteDataSource
) : UsuarioRepository {

    override suspend fun getUsers(): Flow<List<Usuarios>> = flow {
        val result = dataSource.getUsers()
        when (result) {
            is Resource.Success -> {
                val list = result.data?.map { it.toDomain() } ?: emptyList()
                emit(list)
            }
            is Resource.Error -> {
                emit(emptyList())
            }
            is Resource.Loading -> {
                emit(emptyList())
            }
        }
    }

    override suspend fun getUser(id: String): Resource<Usuarios> {
        val result = dataSource.getUser(id)
        return when (result) {
            is Resource.Success -> {
                val usuario = result.data?.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Usuario no encontrado")
                }
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error al obtener el usuario")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }

    override suspend fun register(createUser: CreateUser): Resource<Usuarios> {
        val result = dataSource.register(createUser.toDto())
        return when (result) {
            is Resource.Success -> {
                val usuario = result.data?.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Error al registrar usuario")
                }
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error al registrar usuario")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }

    override suspend fun login(loginUser: LoginUser): Resource<LoginResult> {
        val result = dataSource.login(loginUser.toDto())
        return when (result) {
            is Resource.Success -> {
                val loginResponse = result.data
                if (loginResponse != null) {
                    Resource.Success(loginResponse.toDomain(loginUser.email))
                } else {
                    Resource.Error("Error al iniciar sesión")
                }
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Email o contraseña incorrectos")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }

    override suspend fun updateUser(id: String, updateUser: UpdateUser): Resource<Unit> {
        return dataSource.updateUser(id, updateUser.toDto())
    }

    override suspend fun deleteUser(id: String): Resource<Unit> {
        return dataSource.deleteUser(id)
    }

    override suspend fun getUserByEmail(email: String): Resource<Usuarios> {
        val result = dataSource.getUserByEmail(email)
        return when (result) {
            is Resource.Success -> {
                val usuario = result.data?.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Usuario no encontrado")
                }
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error al obtener el usuario")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }

    override suspend fun test(): Resource<Map<String, Any>> {
        return dataSource.test()
    }
}