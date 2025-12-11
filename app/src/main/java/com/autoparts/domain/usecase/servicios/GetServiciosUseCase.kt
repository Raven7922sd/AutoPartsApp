package com.autoparts.domain.usecase.servicios

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Servicio
import com.autoparts.domain.repository.ServiciosRepository
import javax.inject.Inject

class GetServiciosUseCase @Inject constructor(
    private val repository: ServiciosRepository
) {
    suspend operator fun invoke(): Resource<List<Servicio>> {
        return repository.getServicios()
    }
}