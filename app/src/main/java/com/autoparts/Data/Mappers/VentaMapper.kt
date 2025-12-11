package com.autoparts.Data.Mappers

import com.autoparts.Data.Remote.Dto.*
import com.autoparts.dominio.model.*

fun VentaResponseDto.toDomain(): Venta = Venta(
    ventaId = ventaId,
    applicationUserId = applicationUserId,
    fecha = fecha,
    total = total,
    detalles = detalles.map { it.toDomain() },
    pago = pago.toDomain()
)

fun Venta.toDto(): VentaResponseDto = VentaResponseDto(
    ventaId = ventaId,
    applicationUserId = applicationUserId,
    fecha = fecha,
    total = total,
    detalles = detalles.map { it.toDto() },
    pago = pago.toDto()
)

fun VentaDetalleResponseDto.toDomain(): VentaDetalle = VentaDetalle(
    detalleId = detalleId,
    productoId = productoId,
    productoNombre = productoNombre,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal
)

fun VentaDetalle.toDto(): VentaDetalleResponseDto = VentaDetalleResponseDto(
    detalleId = detalleId,
    productoId = productoId,
    productoNombre = productoNombre,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal
)

fun PagoInfoDto.toDomain(): PagoInfo = PagoInfo(
    pagoId = pagoId,
    nombreTitular = nombreTitular,
    numeroTarjetaEnmascarado = numeroTarjetaEnmascarado,
    direccion = direccion
)

fun PagoInfo.toDto(): PagoInfoDto = PagoInfoDto(
    pagoId = pagoId,
    nombreTitular = nombreTitular,
    numeroTarjetaEnmascarado = numeroTarjetaEnmascarado,
    direccion = direccion
)

fun CreateVenta.toDto(): CreateVentaDto = CreateVentaDto(
    pago = PagoDto(
        nombreTitular = nombreTitular,
        numeroTarjeta = numeroTarjeta,
        fechaExpiracion = fechaExpiracion,
        cvv = cvv,
        direccion = direccion
    )
)