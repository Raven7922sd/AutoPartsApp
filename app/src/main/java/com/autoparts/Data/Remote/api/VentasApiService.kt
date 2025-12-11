package com.autoparts.Data.Remote.api

import com.autoparts.Data.Remote.Dto.CreateVentaDto
import com.autoparts.Data.Remote.Dto.VentaResponseDto
import retrofit2.http.*

interface VentasApiService {

    @GET("api/Ventas")
    suspend fun getVentas(): List<VentaResponseDto>

    @GET("api/Ventas/{ventaId}")
    suspend fun getVenta(@Path("ventaId") ventaId: Int): VentaResponseDto

    @POST("api/Ventas/checkout")
    suspend fun processCheckout(@Body createVentaDto: CreateVentaDto): VentaResponseDto
}