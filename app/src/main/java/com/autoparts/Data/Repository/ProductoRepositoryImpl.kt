package com.autoparts.Data.Repository

import com.autoparts.Data.Mappers.toDomain
import com.autoparts.Data.Mappers.toDto
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.Remote.ProductoRemoteDataSource
import com.autoparts.dominio.model.Producto
import com.autoparts.dominio.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    val dataSource: ProductoRemoteDataSource
): ProductoRepository {
    override suspend fun getProductos(): Flow<List<Producto>> = flow{
        val result = dataSource.getProductos()
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

    override suspend fun getProducto(id: Int): Resource<Producto?> {
        val result = dataSource.getProducto(id)
        return when(result){
            is Resource.Success -> {
                val producto = result.data
                Resource.Success(producto?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> Resource.Error("Error obtener el producto")
        }
    }

    override suspend fun putProducto(id: Int, producto: Producto): Resource<Unit> {
        return dataSource.putProducto(id, producto.toDto())
    }

    override suspend fun postProducto(producto: Producto): Resource<Producto?> {
        val result = dataSource.postProducto(producto.toDto())
        return when(result){
            is Resource.Success -> {
                val producto = result.data
                Resource.Success(producto?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> Resource.Error("Error obtener el producto")
        }
    }
}

