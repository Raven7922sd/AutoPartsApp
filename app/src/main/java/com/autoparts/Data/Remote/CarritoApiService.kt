package com.autoparts.Data.Remote

import com.autoparts.Data.Remote.Dto.CarritoDto
import com.autoparts.Data.Remote.Dto.CarritoTotalDto
import com.autoparts.Data.Remote.Dto.AddCarritoDto
import com.autoparts.Data.Remote.Dto.UpdateCarritoDto
import retrofit2.http.*

interface CarritoApiService {

    @GET("api/Carrito")
    suspend fun getCarrito(): List<CarritoDto>

    @GET("api/Carrito/total")
    suspend fun getCarritoTotal(): CarritoTotalDto

    @POST("api/Carrito")
    suspend fun addItem(@Body addCarritoDto: AddCarritoDto): CarritoDto

    @PUT("api/Carrito/{carritoId}")
    suspend fun updateItem(
        @Path("carritoId") carritoId: Int,
        @Body updateCarritoDto: UpdateCarritoDto
    )

    @DELETE("api/Carrito/{carritoId}")
    suspend fun deleteItem(@Path("carritoId") carritoId: Int)

    @DELETE("api/Carrito/clear")
    suspend fun clearCarrito()
}

