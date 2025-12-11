package com.autoparts.data.repository

import com.autoparts.data.local.manager.CarritoLocalManager
import com.autoparts.data.remote.datasource.CarritoRemoteDataSource
import com.autoparts.data.remote.dto.carrito.AddCarritoRequest
import com.autoparts.data.remote.dto.carrito.UpdateCarritoRequest
import com.autoparts.data.remote.mapper.CarritoMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.AddCarrito
import com.autoparts.domain.model.Carrito
import com.autoparts.domain.model.CarritoTotal
import com.autoparts.domain.model.UpdateCarrito
import com.autoparts.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val remoteDataSource: CarritoRemoteDataSource,
    private val carritoLocalManager: CarritoLocalManager
) : CarritoRepository {

    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
        private const val ERROR_CONEXION = "Error de conexión. Verifica tu internet."
    }

    override suspend fun getCarrito(): Resource<List<Carrito>> {
        return when (val result = remoteDataSource.getCarrito()) {
            is Resource.Success -> {
                val carritos = result.data?.map { CarritoMapper.toDomain(it) } ?: emptyList()
                Resource.Success(carritos)
            }
            is Resource.Error -> {
                val localItems = carritoLocalManager.getItems().first()
                if (localItems.isNotEmpty()) {
                    Resource.Success(emptyList())
                } else {
                    Resource.Error(result.message ?: ERROR_DESCONOCIDO)
                }
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getCarritoTotal(): Resource<CarritoTotal> {
        return when (val result = remoteDataSource.getCarritoTotal()) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(CarritoMapper.totalToDomain(dto))
                } ?: Resource.Error("Error al obtener total")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun addCarritoItem(addCarrito: AddCarrito): Resource<Carrito> {
        carritoLocalManager.addItem(addCarrito)

        val request = AddCarritoRequest(
            productoId = addCarrito.productoId,
            cantidad = addCarrito.cantidad
        )

        return when (val result = remoteDataSource.addCarritoItem(request)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(CarritoMapper.toDomain(dto))
                } ?: Resource.Error("Error al agregar item")
            }
            is Resource.Error -> {
                Resource.Success(Carrito(
                    carritoId = 0,
                    applicationUserId = "",
                    productoId = addCarrito.productoId,
                    producto = null,
                    cantidad = addCarrito.cantidad
                ))
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun updateCarritoItem(carritoId: Int, updateCarrito: UpdateCarrito): Resource<Unit> {
        val request = UpdateCarritoRequest(cantidad = updateCarrito.cantidad)
        return remoteDataSource.updateCarritoItem(carritoId, request)
    }

    override suspend fun deleteCarritoItem(carritoId: Int): Resource<Unit> {
        return remoteDataSource.deleteCarritoItem(carritoId)
    }

    override suspend fun clearCarrito(): Resource<Unit> {
        carritoLocalManager.clearCarrito()
        return remoteDataSource.clearCarrito()
    }
}