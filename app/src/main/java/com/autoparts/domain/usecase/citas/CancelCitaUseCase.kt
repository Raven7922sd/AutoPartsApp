package com.autoparts.domain.usecase.citas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.repository.CitasRepository
import javax.inject.Inject

class CancelCitaUseCase @Inject constructor(
    private val repository: CitasRepository
) {
    suspend operator fun invoke(citaId: Int): Resource<Unit> {
        return repository.cancelCita(citaId)
    }
}