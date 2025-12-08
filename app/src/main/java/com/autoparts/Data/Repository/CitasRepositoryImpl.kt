package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.CitasRemoteDataSource
import com.autoparts.data.remote.dto.cita.CreateCitaRequest
import com.autoparts.data.remote.mapper.CitaMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Cita
import com.autoparts.domain.model.CreateCita
import com.autoparts.domain.repository.CitasRepository
import javax.inject.Inject

class CitasRepositoryImpl @Inject constructor(
    private val remoteDataSource: CitasRemoteDataSource
) : CitasRepository {

    override suspend fun getCitas(): Resource<List<Cita>> {
        return when (val result = remoteDataSource.getCitas()) {
            is Resource.Success -> {
                val citas = result.data?.map { CitaMapper.toDomain(it) } ?: emptyList()
                Resource.Success(citas)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun createCita(cita: CreateCita): Resource<Cita> {
        val request = CreateCitaRequest(
            clienteNombre = cita.clienteNombre,
            servicioSolicitado = cita.servicioSolicitado,
            fechaCita = cita.fechaCita
        )

        return when (val result = remoteDataSource.createCita(request)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(CitaMapper.toDomain(dto))
                } ?: Resource.Error("Error al crear cita")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun confirmarCita(citaId: Int): Resource<Cita> {
        return when (val result = remoteDataSource.confirmarCita(citaId)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(CitaMapper.toDomain(dto))
                } ?: Resource.Error("Error al confirmar cita")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun cancelCita(citaId: Int): Resource<Unit> {
        return remoteDataSource.cancelCita(citaId)
    }
}