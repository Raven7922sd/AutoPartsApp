package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.ProductosApiService
import com.autoparts.data.remote.dto.producto.ProductoDto
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class ProductoRemoteDataSource @Inject constructor(
    private val apiService: ProductosApiService
) {
    suspend fun getProductos(): Resource<List<ProductoDto>> {
        return try {
            val response = apiService.getProductos()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(message = "Error al cargar productos: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error de conexión")
        }
    }

    suspend fun getProducto(id: Int): Resource<ProductoDto> {
        return try {
            val response = apiService.getProducto(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Producto no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error de conexión")
        }
    }
}