package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.carrito.CarritoDto
import com.autoparts.data.remote.dto.carrito.CarritoTotalResponse
import com.autoparts.domain.model.Carrito
import com.autoparts.domain.model.CarritoTotal

object CarritoMapper {
    fun toDomain(dto: CarritoDto): Carrito {
        return Carrito(
            carritoId = dto.carritoId,
            applicationUserId = dto.applicationUserId,
            productoId = dto.productoId,
            producto = dto.producto?.let { ProductoMapper.toDomain(it) },
            cantidad = dto.cantidad
        )
    }

    fun totalToDomain(dto: CarritoTotalResponse): CarritoTotal {
        return CarritoTotal(
            totalItems = dto.totalItems,
            totalPrice = dto.totalPrice
        )
    }
}