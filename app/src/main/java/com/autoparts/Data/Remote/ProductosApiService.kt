package com.autoparts.Data.Remote

import com.autoparts.Data.Remote.Dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductosApiService {
    @GET("api/Productos")
    suspend fun getProductos(): Response<List<ProductoDto>>

    @GET("api/Productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): Response<ProductoDto?>

    @PUT("api/Productos/{id}")
    suspend fun putProducto(@Path("id") id: Int, @Body producto: ProductoDto): Response<Unit>

    @POST("api/Productos")
    suspend fun postProducto(@Body producto: ProductoDto): Response<ProductoDto?>
}

