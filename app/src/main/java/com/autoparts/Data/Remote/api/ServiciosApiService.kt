package com.autoparts.data.remote.api

import com.autoparts.data.remote.dto.servicio.ServicioDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiciosApiService {
    @GET("api/Servicios")
    suspend fun getServicios(): Response<List<ServicioDto>>

    @GET("api/Servicios/{id}")
    suspend fun getServicio(@Path("id") id: Int): Response<ServicioDto>
}