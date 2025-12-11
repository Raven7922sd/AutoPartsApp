package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.ProductoRemoteDataSource
import com.autoparts.data.remote.mapper.ProductoMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Producto
import com.autoparts.domain.repository.ProductoRepository
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductoRemoteDataSource
) : ProductoRepository {

    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
    }

    override suspend fun getProductos(): Resource<List<Producto>> {
        return when (val result = remoteDataSource.getProductos()) {
            is Resource.Success -> {
                val productos = result.data?.map { ProductoMapper.toDomain(it) } ?: emptyList()
                Resource.Success(productos)
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getProducto(id: Int): Resource<Producto> {
        return when (val result = remoteDataSource.getProducto(id)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(ProductoMapper.toDomain(dto))
                } ?: Resource.Error("Producto no encontrado")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }
}