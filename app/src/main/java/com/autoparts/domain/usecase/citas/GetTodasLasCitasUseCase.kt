package com.autoparts.domain.usecase.citas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Cita
import com.autoparts.domain.repository.CitasRepository
import javax.inject.Inject

class GetTodasLasCitasUseCase @Inject constructor(
    private val repository: CitasRepository
) {
    suspend operator fun invoke(pagina: Int = 1, tamanoPagina: Int = 10): Resource<List<Cita>> {
        return repository.getTodasLasCitas(pagina, tamanoPagina)
    }
}