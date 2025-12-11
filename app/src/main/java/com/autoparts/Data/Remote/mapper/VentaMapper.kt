package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.venta.PagoInfoDto
import com.autoparts.data.remote.dto.venta.VentaDetalleDto
import com.autoparts.data.remote.dto.venta.VentaDto
import com.autoparts.domain.model.PagoInfo
import com.autoparts.domain.model.Venta
import com.autoparts.domain.model.VentaDetalle

object VentaMapper {
    fun toDomain(dto: VentaDto): Venta {
        return Venta(
            ventaId = dto.ventaId,
            applicationUserId = dto.applicationUserId,
            fecha = dto.fecha,
            total = dto.total,
            detalles = dto.detalles.map { detalleToDomain(it) },
            pago = pagoToDomain(dto.pago)
        )
    }

    private fun detalleToDomain(dto: VentaDetalleDto): VentaDetalle {
        return VentaDetalle(
            detalleId = dto.detalleId,
            productoId = dto.productoId,
            productoNombre = dto.productoNombre,
            cantidad = dto.cantidad,
            precioUnitario = dto.precioUnitario,
            subtotal = dto.subtotal
        )
    }

    private fun pagoToDomain(dto: PagoInfoDto): PagoInfo {
        return PagoInfo(
            pagoId = dto.pagoId,
            nombreTitular = dto.nombreTitular,
            numeroTarjetaEnmascarado = dto.numeroTarjetaEnmascarado,
            direccion = dto.direccion
        )
    }
}