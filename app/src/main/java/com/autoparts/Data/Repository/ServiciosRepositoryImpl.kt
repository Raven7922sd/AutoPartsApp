package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.ServiciosRemoteDataSource
import com.autoparts.data.remote.mapper.ServicioMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Servicio
import com.autoparts.domain.repository.ServiciosRepository
import javax.inject.Inject

class ServiciosRepositoryImpl @Inject constructor(
    private val remoteDataSource: ServiciosRemoteDataSource
) : ServiciosRepository {

    override suspend fun getServicios(): Resource<List<Servicio>> {
        return when (val result = remoteDataSource.getServicios()) {
            is Resource.Success -> {
                val servicios = result.data?.map { ServicioMapper.toDomain(it) } ?: emptyList()
                Resource.Success(servicios)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getServicio(id: Int): Resource<Servicio> {
        return when (val result = remoteDataSource.getServicio(id)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(ServicioMapper.toDomain(dto))
                } ?: Resource.Error("Servicio no encontrado")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }
}