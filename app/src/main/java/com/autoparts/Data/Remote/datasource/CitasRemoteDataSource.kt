package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.CitasApiService
import com.autoparts.data.remote.dto.cita.CitaDto
import com.autoparts.data.remote.dto.cita.CreateCitaRequest
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class CitasRemoteDataSource @Inject constructor(
    private val apiService: CitasApiService
) {
    suspend fun getCitas(): Resource<List<CitaDto>> {
        return try {
            val response = apiService.getCitas()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }

    suspend fun createCita(createCitaRequest: CreateCitaRequest): Resource<CitaDto> {
        return try {
            val response = apiService.createCita(createCitaRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al crear cita")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }

    suspend fun confirmarCita(citaId: Int): Resource<CitaDto> {
        return try {
            val response = apiService.confirmarCita(citaId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al confirmar cita: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }

    suspend fun cancelCita(citaId: Int): Resource<Unit> {
        return try {
            val response = apiService.cancelCita(citaId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Error al cancelar cita")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }
}