package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.ServiciosApiService
import com.autoparts.data.remote.dto.servicio.ServicioDto
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class ServiciosRemoteDataSource @Inject constructor(
    private val apiService: ServiciosApiService
) {
    suspend fun getServicios(): Resource<List<ServicioDto>> {
        return try {
            val response = apiService.getServicios()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }

    suspend fun getServicio(id: Int): Resource<ServicioDto> {
        return try {
            val response = apiService.getServicio(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Servicio no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error desconocido")
        }
    }
}