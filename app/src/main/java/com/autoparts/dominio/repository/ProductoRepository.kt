package com.autoparts.dominio.repository

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    suspend fun getProductos(): Flow<List<Producto>>
    suspend fun getProducto(id: Int): Resource<Producto?>
    suspend fun putProducto(id: Int, producto: Producto): Resource<Unit>
    suspend fun postProducto(producto: Producto): Resource<Producto?>
}

