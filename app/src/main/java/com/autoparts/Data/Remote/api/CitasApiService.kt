package com.autoparts.data.remote.api

import com.autoparts.data.remote.dto.cita.CitaDto
import com.autoparts.data.remote.dto.cita.CreateCitaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CitasApiService {
    @GET("api/Citas")
    suspend fun getCitas(): Response<List<CitaDto>>

    @POST("api/Citas")
    suspend fun createCita(@Body createCitaRequest: CreateCitaRequest): Response<CitaDto>

    @POST("api/Citas/{citaId}/confirmar")
    suspend fun confirmarCita(@Path("citaId") citaId: Int): Response<CitaDto>

    @DELETE("api/Citas/{citaId}")
    suspend fun cancelCita(@Path("citaId") citaId: Int): Response<Unit>
}