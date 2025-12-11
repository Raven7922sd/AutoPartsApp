package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Servicio

interface ServiciosRepository {
    suspend fun getServicios(): Resource<List<Servicio>>
    suspend fun getServicio(id: Int): Resource<Servicio>
}