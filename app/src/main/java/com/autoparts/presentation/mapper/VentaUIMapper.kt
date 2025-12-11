package com.autoparts.presentation.mapper

import com.autoparts.domain.model.PagoInfo
import com.autoparts.domain.model.Venta
import com.autoparts.domain.model.VentaDetalle
import com.autoparts.presentation.ventadetalle.PagoInfoUI
import com.autoparts.presentation.ventadetalle.VentaDetalleUI
import com.autoparts.presentation.ventadetalle.VentaUI
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object VentaUIMapper {

    fun toUI(venta: Venta): VentaUI {
        return VentaUI(
            ventaId = venta.ventaId,
            fecha = venta.fecha,
            fechaFormateada = formatearFecha(venta.fecha),
            total = venta.total,
            totalFormateado = formatearMonto(venta.total),
            detalles = venta.detalles.map { toDetalleUI(it) },
            pagoInfo = toPagoInfoUI(venta.pago)
        )
    }

    private fun toDetalleUI(detalle: VentaDetalle): VentaDetalleUI {
        return VentaDetalleUI(
            detalleId = detalle.detalleId,
            productoId = detalle.productoId,
            productoNombre = detalle.productoNombre,
            cantidad = detalle.cantidad,
            precioUnitario = detalle.precioUnitario,
            subtotal = detalle.subtotal,
            descripcionCantidad = "${detalle.cantidad.toInt()} x ${formatearMonto(detalle.precioUnitario)}",
            subtotalFormateado = formatearMonto(detalle.subtotal)
        )
    }

    private fun toPagoInfoUI(pagoInfo: PagoInfo): PagoInfoUI {
        return PagoInfoUI(
            nombreTitular = pagoInfo.nombreTitular,
            numeroTarjetaEnmascarado = pagoInfo.numeroTarjetaEnmascarado,
            direccion = pagoInfo.direccion
        )
    }

    fun toUIList(ventas: List<Venta>): List<VentaUI> {
        return ventas.map { toUI(it) }
    }

    private fun formatearFecha(fecha: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(fecha)
            date?.let { outputFormat.format(it) } ?: fecha
        } catch (e: Exception) {
            fecha
        }
    }

    private fun formatearMonto(monto: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-DO"))
        return formatter.format(monto)
    }
}