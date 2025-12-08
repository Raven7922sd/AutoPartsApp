package com.autoparts.data.remote.dto.ventas

import com.google.gson.annotations.SerializedName

data class EstadisticasVentasDto(
    @SerializedName("resumen")
    val resumen: ResumenDto,

    @SerializedName("ventasPorMes")
    val ventasPorMes: List<VentaMensualDto>,

    @SerializedName("productosMasVendidos")
    val productosMasVendidos: List<ProductoVendidoDto>
)

data class ResumenDto(
    @SerializedName("totalVentas")
    val totalVentas: Int,

    @SerializedName("totalIngresos")
    val totalIngresos: Double,

    @SerializedName("promedioVenta")
    val promedioVenta: Double
)

data class VentaMensualDto(
    @SerializedName("anio")
    val anio: Int,

    @SerializedName("mes")
    val mes: Int,

    @SerializedName("totalVentas")
    val totalVentas: Int,

    @SerializedName("totalIngresos")
    val totalIngresos: Double
)

data class ProductoVendidoDto(
    @SerializedName("productoId")
    val productoId: Int,

    @SerializedName("productoNombre")
    val productoNombre: String,

    @SerializedName("cantidadVendida")
    val cantidadVendida: Double,

    @SerializedName("totalIngresos")
    val totalIngresos: Double
)