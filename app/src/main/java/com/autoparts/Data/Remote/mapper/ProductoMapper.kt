package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.producto.ProductoDto
import com.autoparts.domain.model.Producto

object ProductoMapper {
    fun toDomain(dto: ProductoDto): Producto {
        return Producto(
            productoId = dto.productoId,
            productoNombre = dto.productoNombre,
            productoMonto = dto.productoMonto,
            productoCantidad = dto.productoCantidad,
            productoDescripcion = dto.productoDescripcion,
            productoImagenUrl = dto.productoImagenUrl,
            categoria = dto.categoria,
            fecha = dto.fecha
        )
    }
}