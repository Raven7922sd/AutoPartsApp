package com.autoparts.data.remote.api

import com.autoparts.data.remote.dto.carrito.AddCarritoRequest
import com.autoparts.data.remote.dto.carrito.CarritoDto
import com.autoparts.data.remote.dto.carrito.CarritoTotalResponse
import com.autoparts.data.remote.dto.carrito.UpdateCarritoRequest
import retrofit2.Response
import retrofit2.http.*

interface CarritoApiService {
    @GET("api/Carrito")
    suspend fun getCarrito(): Response<List<CarritoDto>>

    @GET("api/Carrito/total")
    suspend fun getCarritoTotal(): Response<CarritoTotalResponse>

    @POST("api/Carrito")
    suspend fun addCarritoItem(@Body addCarritoRequest: AddCarritoRequest): Response<CarritoDto>

    @PUT("api/Carrito/{carritoId}")
    suspend fun updateCarritoItem(
        @Path("carritoId") carritoId: Int,
        @Body updateCarritoRequest: UpdateCarritoRequest
    ): Response<Unit>

    @DELETE("api/Carrito/{carritoId}")
    suspend fun deleteCarritoItem(@Path("carritoId") carritoId: Int): Response<Unit>

    @DELETE("api/Carrito/clear")
    suspend fun clearCarrito(): Response<Unit>
}