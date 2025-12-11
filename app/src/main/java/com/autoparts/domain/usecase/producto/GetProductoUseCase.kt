package com.autoparts.domain.usecase.producto

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Producto
import com.autoparts.domain.repository.ProductoRepository
import javax.inject.Inject

class GetProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int): Resource<Producto> {
        return repository.getProducto(id)
    }
}