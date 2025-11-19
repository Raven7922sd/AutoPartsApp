package com.autoparts.Data.Repository

import com.autoparts.Data.Mappers.toDomain
import com.autoparts.Data.Mappers.toDto
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.Remote.UsuarioRemoteDataSource
import com.autoparts.dominio.model.Usuarios
import com.autoparts.dominio.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    val dataSource: UsuarioRemoteDataSource
): UsuarioRepository {
    override suspend fun getUsuarios(): Flow<List<Usuarios>> = flow{
        val result = dataSource.getUsuarios()
        when(result){
            is Resource.Success -> {
                val list = result.data?.map{it.toDomain()} ?: emptyList()
                emit(list)
            }
            is Resource.Error -> {
                emit(emptyList())
            }
            else -> emit(emptyList())
        }
    }

    override suspend fun getUsuario(id: Int): Resource<Usuarios?> {
        val result = dataSource.getUsuario(id)
        return when(result){
            is Resource.Success -> {
                val usuario = result.data
                Resource.Success(usuario?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> Resource.Error("Error obtener el usuario")
        }
    }

    override suspend fun putUsuario(id: Int, usuario: Usuarios): Resource<Unit> {
        return dataSource.putUsuario(id, usuario.toDto())
    }

    override suspend fun postUsuario(usuario: Usuarios): Resource<Usuarios?> {
        val result = dataSource.postUsuario(usuario.toDto())
        return when(result){
            is Resource.Success -> {
                val usuario = result.data
                Resource.Success(usuario?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> Resource.Error("Error obtener el usuario")
        }
    }
}