package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Cita
import com.autoparts.domain.model.CreateCita

interface CitasRepository {
    suspend fun getCitas(): Resource<List<Cita>>
    suspend fun createCita(cita: CreateCita): Resource<Cita>
    suspend fun confirmarCita(citaId: Int): Resource<Cita>
    suspend fun cancelCita(citaId: Int): Resource<Unit>
}