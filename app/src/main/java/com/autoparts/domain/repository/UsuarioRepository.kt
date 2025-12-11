package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateUser
import com.autoparts.domain.model.UpdateUser
import com.autoparts.domain.model.Usuarios

interface UsuarioRepository {
    suspend fun register(createUser: CreateUser): Resource<Usuarios>
    suspend fun login(email: String, password: String): Resource<Usuarios>
    suspend fun logout()
    suspend fun updateUsuario(id: String, updateUser: UpdateUser): Resource<Unit>
    suspend fun getUsuarioByEmail(email: String): Resource<Usuarios>
}