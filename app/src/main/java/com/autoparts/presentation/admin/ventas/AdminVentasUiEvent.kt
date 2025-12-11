package com.autoparts.presentation.admin.ventas

import com.autoparts.domain.model.VentaDetallada

sealed interface AdminVentasUiEvent {
    data class LoadVentas(
        val pagina: Int = 1,
        val tamanoPagina: Int = 10,
        val fechaDesde: String? = null,
        val fechaHasta: String? = null,
        val usuarioId: String? = null
    ) : AdminVentasUiEvent

    data class ShowDetalleVenta(val venta: VentaDetallada) : AdminVentasUiEvent
    data object CloseDetalleDialog : AdminVentasUiEvent
    data object ClearError : AdminVentasUiEvent
    data class OnSearchQueryChanged(val query: String) : AdminVentasUiEvent
    data class OnFiltroOrdenChanged(val filtro: FiltroOrdenVenta) : AdminVentasUiEvent
    data class ChangePage(val pagina: Int) : AdminVentasUiEvent
    data class ApplyFilters(
        val fechaDesde: String?,
        val fechaHasta: String?,
        val usuarioId: String?
    ) : AdminVentasUiEvent
    data object ClearFilters : AdminVentasUiEvent
    data class LoadEstadisticas(
        val fechaDesde: String? = null,
        val fechaHasta: String? = null
    ) : AdminVentasUiEvent
}