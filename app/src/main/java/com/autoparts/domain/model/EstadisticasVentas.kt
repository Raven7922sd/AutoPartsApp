package com.autoparts.domain.model

data class EstadisticasVentas(
    val resumen: ResumenVentas,
    val ventasPorMes: List<VentaMensual>,
    val productosMasVendidos: List<ProductoVendido>
)

data class ResumenVentas(
    val totalVentas: Int,
    val totalIngresos: Double,
    val promedioVenta: Double
)

data class VentaMensual(
    val anio: Int,
    val mes: Int,
    val totalVentas: Int,
    val totalIngresos: Double
)

data class ProductoVendido(
    val productoId: Int,
    val productoNombre: String,
    val cantidadVendida: Double,
    val totalIngresos: Double
)