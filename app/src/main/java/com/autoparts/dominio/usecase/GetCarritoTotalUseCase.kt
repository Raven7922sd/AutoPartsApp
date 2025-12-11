package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CarritoTotal
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class GetCarritoTotalUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(): Resource<CarritoTotal> {
        return repository.getCarritoTotal()
    }
}

