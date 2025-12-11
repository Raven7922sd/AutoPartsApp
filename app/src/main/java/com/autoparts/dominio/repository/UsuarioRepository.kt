package com.autoparts.dominio.repository

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CreateUser
import com.autoparts.dominio.model.LoginResult
import com.autoparts.dominio.model.LoginUser
import com.autoparts.dominio.model.UpdateUser
import com.autoparts.dominio.model.Usuarios
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun getUsers(): Flow<List<Usuarios>>

    suspend fun getUser(id: String): Resource<Usuarios>

    suspend fun register(createUser: CreateUser): Resource<Usuarios>

    suspend fun login(loginUser: LoginUser): Resource<LoginResult>

    suspend fun updateUser(id: String, updateUser: UpdateUser): Resource<Unit>

    suspend fun deleteUser(id: String): Resource<Unit>

    suspend fun getUserByEmail(email: String): Resource<Usuarios>

    suspend fun test(): Resource<Map<String, Any>>
}