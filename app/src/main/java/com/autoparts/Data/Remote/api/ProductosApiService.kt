package com.autoparts.data.remote.api

import com.autoparts.data.remote.dto.producto.ProductoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductosApiService {
    @GET("api/Productos")
    suspend fun getProductos(): Response<List<ProductoDto>>

    @GET("api/Productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): Response<ProductoDto>
}