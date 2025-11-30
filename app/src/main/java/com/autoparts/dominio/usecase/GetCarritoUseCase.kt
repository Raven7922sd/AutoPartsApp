package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class GetCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(): Resource<List<Carrito>> {
        return repository.getCarrito()
    }
}

