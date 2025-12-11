package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.ventas.*
import com.autoparts.domain.model.*

object EstadisticasMapper {

    fun toDomain(dto: EstadisticasVentasDto): EstadisticasVentas {
        return EstadisticasVentas(
            resumen = toDomain(dto.resumen),
            ventasPorMes = dto.ventasPorMes.map { toDomain(it) },
            productosMasVendidos = dto.productosMasVendidos.map { toDomain(it) }
        )
    }

    fun toDomain(dto: ResumenDto): ResumenVentas {
        return ResumenVentas(
            totalVentas = dto.totalVentas,
            totalIngresos = dto.totalIngresos,
            promedioVenta = dto.promedioVenta
        )
    }

    fun toDomain(dto: VentaMensualDto): VentaMensual {
        return VentaMensual(
            anio = dto.anio,
            mes = dto.mes,
            totalVentas = dto.totalVentas,
            totalIngresos = dto.totalIngresos
        )
    }

    fun toDomain(dto: ProductoVendidoDto): ProductoVendido {
        return ProductoVendido(
            productoId = dto.productoId,
            productoNombre = dto.productoNombre,
            cantidadVendida = dto.cantidadVendida,
            totalIngresos = dto.totalIngresos
        )
    }
}