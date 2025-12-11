package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.ventas.*
import com.autoparts.domain.model.*

object VentasAdminMapper {

    fun toDomain(dto: VentasPaginadasResponseDto): VentasPaginadas {
        return VentasPaginadas(
            ventas = dto.ventas.map { toVentaDetallada(it) },
            paginaActual = dto.paginaActual,
            totalPaginas = dto.totalPaginas,
            totalVentas = dto.totalVentas,
            totalIngresos = dto.totalIngresos
        )
    }

    fun toVentaDetallada(dto: VentaResponseDto): VentaDetallada {
        return VentaDetallada(
            ventaId = dto.ventaId,
            applicationUserId = dto.applicationUserId,
            nombreUsuario = dto.nombreUsuario,
            emailUsuario = dto.emailUsuario,
            fecha = dto.fecha,
            total = dto.total,
            detalles = dto.detalles.map { toVentaDetalleInfo(it) },
            pago = PagoInfo(
                pagoId = dto.pago.pagoId,
                nombreTitular = dto.pago.nombreTitular,
                numeroTarjetaEnmascarado = dto.pago.numeroTarjetaEnmascarado,
                direccion = dto.pago.direccion
            )
        )
    }

    fun toVentaDetalleInfo(dto: VentaDetalleResponseDto): VentaDetalleInfo {
        return VentaDetalleInfo(
            detalleId = dto.detalleId,
            productoId = dto.productoId,
            productoNombre = dto.productoNombre,
            cantidad = dto.cantidad,
            precioUnitario = dto.precioUnitario,
            subtotal = dto.subtotal
        )
    }
}