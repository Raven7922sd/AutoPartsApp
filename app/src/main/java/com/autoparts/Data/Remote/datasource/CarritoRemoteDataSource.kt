package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.CarritoApiService
import com.autoparts.data.remote.dto.carrito.CarritoDto
import com.autoparts.data.remote.dto.carrito.AddCarritoRequest
import com.autoparts.data.remote.dto.carrito.UpdateCarritoRequest
import com.autoparts.data.remote.dto.carrito.CarritoTotalResponse
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class CarritoRemoteDataSource @Inject constructor(
    private val apiService: CarritoApiService
) {
    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
    }

    suspend fun getCarrito(): Resource<List<CarritoDto>> {
        return try {
            val response = apiService.getCarrito()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun getCarritoTotal(): Resource<CarritoTotalResponse> {
        return try {
            val response = apiService.getCarritoTotal()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al obtener total")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun addCarritoItem(addCarritoRequest: AddCarritoRequest): Resource<CarritoDto> {
        return try {
            val response = apiService.addCarritoItem(addCarritoRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al agregar item")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun updateCarritoItem(carritoId: Int, updateCarritoRequest: UpdateCarritoRequest): Resource<Unit> {
        return try {
            val response = apiService.updateCarritoItem(carritoId, updateCarritoRequest)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Error al actualizar item")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun deleteCarritoItem(carritoId: Int): Resource<Unit> {
        return try {
            val response = apiService.deleteCarritoItem(carritoId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Error al eliminar item")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun clearCarrito(): Resource<Unit> {
        return try {
            val response = apiService.clearCarrito()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Error al limpiar carrito")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }
}