package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Producto
import com.autoparts.dominio.repository.ProductoRepository
import javax.inject.Inject

class SaveProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(producto: Producto): Resource<Producto?> {
        return if(producto.productoId == null) {
            repository.postProducto(producto)
        } else {
            val updateResult = repository.putProducto(producto.productoId, producto)
            when(updateResult){
                is Resource.Success -> Resource.Success(producto)
                is Resource.Error -> Resource.Error(updateResult.message ?: "Error al actualizar")
                else -> Resource.Error("Error desconocido")
            }
        }
    }
}

