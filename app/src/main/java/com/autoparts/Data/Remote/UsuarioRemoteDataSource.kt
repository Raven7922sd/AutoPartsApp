package com.autoparts.Data.Remote

import com.autoparts.Data.Remote.Dto.CreateUserDto
import com.autoparts.Data.Remote.Dto.LoginDto
import com.autoparts.Data.Remote.Dto.LoginResponse
import com.autoparts.Data.Remote.Dto.UpdateUserDto
import com.autoparts.Data.Remote.Dto.UserDto
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: UsuariosApiService
) {
    suspend fun getUsers(): Resource<List<UserDto>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: No se obtuvieron datos")
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error al obtener los usuarios"}")
        }
    }

    suspend fun getUser(id: String): Resource<UserDto> {
        return try {
            val response = apiService.getUser(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: Usuario no encontrado")
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error al obtener el usuario"}")
        }
    }

    suspend fun register(createUserDto: CreateUserDto): Resource<UserDto> {
        return try {
            val response = apiService.register(createUserDto)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: No se pudo registrar el usuario")
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Error ${response.code()}: ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Error al registrar el usuario"}")
        }
    }

    suspend fun login(loginDto: LoginDto): Resource<LoginResponse> {
        return try {
            val response = apiService.login(loginDto)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: Credenciales incorrectas")
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Error ${response.code()}: ${errorBody ?: "Email o contraseña incorrectos"}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Error al iniciar sesión"}")
        }
    }

    suspend fun updateUser(id: String, updateUserDto: UpdateUserDto): Resource<Unit> {
        return try {
            val response = apiService.updateUser(id, updateUserDto)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Error al actualizar el usuario"}")
        }
    }

    suspend fun deleteUser(id: String): Resource<Unit> {
        return try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Error al eliminar el usuario"}")
        }
    }

    suspend fun getUserByEmail(email: String): Resource<UserDto> {
        return try {
            val response = apiService.getUserByEmail(email)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: Usuario no encontrado")
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error al obtener el usuario"}")
        }
    }

    suspend fun test(): Resource<Map<String, Any>> {
        return try {
            val response = apiService.test()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error: No se pudo realizar el test")
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.localizedMessage ?: "Error al hacer el test"}")
        }
    }
}