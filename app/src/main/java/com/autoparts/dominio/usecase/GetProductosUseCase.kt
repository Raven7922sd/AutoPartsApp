package com.autoparts.dominio.usecase

import com.autoparts.dominio.repository.ProductoRepository
import javax.inject.Inject

class GetProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke() = repository.getProductos()
}

