package com.autoparts.Data.Mappers

import com.autoparts.Data.Remote.Dto.ProductoDto
import com.autoparts.dominio.model.Producto

fun Producto.toDto(): ProductoDto = ProductoDto(
    productoId = productoId,
    productoNombre = productoNombre,
    productoMonto = productoMonto,
    productoCantidad = productoCantidad,
    productoDescripcion = productoDescripcion,
    productoImagenUrl = productoImagenUrl,
    categoria = categoria,
    fecha = fecha
)

fun ProductoDto.toDomain(): Producto = Producto(
    productoId = productoId,
    productoNombre = productoNombre,
    productoMonto = productoMonto,
    productoCantidad = productoCantidad,
    productoDescripcion = productoDescripcion,
    productoImagenUrl = productoImagenUrl,
    categoria = categoria,
    fecha = fecha
)
