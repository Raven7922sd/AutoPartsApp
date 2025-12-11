package com.autoparts.domain.usecase.producto

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Producto
import com.autoparts.domain.repository.ProductoRepository
import javax.inject.Inject

class GetProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(): Resource<List<Producto>> {
        return repository.getProductos()
    }
}