package com.autoparts.domain.usecase.citas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Cita
import com.autoparts.domain.model.CreateCita
import com.autoparts.domain.repository.CitasRepository
import javax.inject.Inject

class CreateCitaUseCase @Inject constructor(
    private val repository: CitasRepository
) {
    suspend operator fun invoke(createCita: CreateCita): Resource<Cita> {
        return repository.createCita(createCita)
    }
}