package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class AddCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(addCarrito: AddCarrito): Resource<Carrito> {
        return repository.addItem(addCarrito)
    }
}