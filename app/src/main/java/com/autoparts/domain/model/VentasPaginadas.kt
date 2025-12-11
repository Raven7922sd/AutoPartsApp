package com.autoparts.domain.model

data class VentasPaginadas(
    val ventas: List<VentaDetallada>,
    val paginaActual: Int,
    val totalPaginas: Int,
    val totalVentas: Int,
    val totalIngresos: Double
)

data class VentaDetallada(
    val ventaId: Int,
    val applicationUserId: String,
    val nombreUsuario: String?,
    val emailUsuario: String?,
    val fecha: String,
    val total: Double,
    val detalles: List<VentaDetalleInfo>,
    val pago: PagoInfo
)

data class VentaDetalleInfo(
    val detalleId: Int,
    val productoId: Int,
    val productoNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val subtotal: Double
)