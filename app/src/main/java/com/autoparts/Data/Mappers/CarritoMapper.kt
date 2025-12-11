package com.autoparts.Data.Mappers

import com.autoparts.Data.Remote.Dto.CarritoDto
import com.autoparts.Data.Remote.Dto.CarritoTotalDto
import com.autoparts.Data.Remote.Dto.AddCarritoDto
import com.autoparts.Data.Remote.Dto.UpdateCarritoDto
import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.model.CarritoTotal
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.dominio.model.UpdateCarrito

fun CarritoDto.toDomain(): Carrito = Carrito(
    carritoId = carritoId,
    applicationUserId = applicationUserId,
    productoId = productoId,
    producto = producto?.toDomain(),
    cantidad = cantidad
)

fun Carrito.toDto(): CarritoDto = CarritoDto(
    carritoId = carritoId,
    applicationUserId = applicationUserId,
    productoId = productoId,
    producto = producto?.toDto(),
    cantidad = cantidad
)

fun CarritoTotalDto.toDomain(): CarritoTotal = CarritoTotal(
    totalItems = totalItems,
    totalPrice = totalPrice
)

fun AddCarrito.toDto(): AddCarritoDto = AddCarritoDto(
    productoId = productoId,
    cantidad = cantidad
)

fun UpdateCarrito.toDto(): UpdateCarritoDto = UpdateCarritoDto(
    cantidad = cantidad
)

