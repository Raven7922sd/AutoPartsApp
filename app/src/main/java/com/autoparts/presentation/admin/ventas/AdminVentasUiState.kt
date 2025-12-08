package com.autoparts.presentation.admin.ventas

import com.autoparts.domain.model.EstadisticasVentas
import com.autoparts.domain.model.VentaDetallada

data class AdminVentasUiState(
    val isLoading: Boolean = false,
    val ventas: List<VentaDetallada> = emptyList(),
    val ventasFiltradas: List<VentaDetallada> = emptyList(),
    val ventaSeleccionada: VentaDetallada? = null,
    val showDetalleDialog: Boolean = false,
    val error: String? = null,
    val totalVentas: Double = 0.0,
    val cantidadVentas: Int = 0,
    val searchQuery: String = "",
    val filtroOrden: FiltroOrdenVenta = FiltroOrdenVenta.MAS_RECIENTE,
    val paginaActual: Int = 1,
    val totalPaginas: Int = 1,
    val fechaDesde: String? = null,
    val fechaHasta: String? = null,
    val usuarioIdFiltro: String? = null,
    val estadisticas: EstadisticasVentas? = null
)

enum class FiltroOrdenVenta {
    MAS_RECIENTE,
    MAS_ANTIGUA,
    MAYOR_MONTO,
    MENOR_MONTO
}