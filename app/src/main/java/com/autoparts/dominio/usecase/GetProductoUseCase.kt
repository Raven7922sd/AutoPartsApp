package com.autoparts.dominio.usecase

import com.autoparts.dominio.repository.ProductoRepository
import javax.inject.Inject

class GetProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int) = repository.getProducto(id)
}

