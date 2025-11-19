package com.autoparts.dominio.repository

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Usuarios
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun getUsuarios(): Flow<List<Usuarios>>
    suspend fun getUsuario(id: Int): Resource<Usuarios?>
    suspend fun putUsuario(id: Int, usuario: Usuarios): Resource<Unit>
    suspend fun postUsuario(usuario: Usuarios): Resource<Usuarios?>
}