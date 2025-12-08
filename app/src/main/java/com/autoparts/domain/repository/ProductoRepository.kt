package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): Resource<List<Producto>>
    suspend fun getProducto(id: Int): Resource<Producto>
}