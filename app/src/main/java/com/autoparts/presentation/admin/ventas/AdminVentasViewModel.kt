package com.autoparts.presentation.admin.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.VentaDetallada
import com.autoparts.domain.usecase.ventas.GetAllVentasUseCase
import com.autoparts.domain.usecase.ventas.GetEstadisticasVentasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminVentasViewModel @Inject constructor(
    private val getAllVentasUseCase: GetAllVentasUseCase,
    private val getEstadisticasVentasUseCase: GetEstadisticasVentasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AdminVentasUiState())
    val state: StateFlow<AdminVentasUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<AdminVentasUiEffect>()
    val effects: SharedFlow<AdminVentasUiEffect> = _effects.asSharedFlow()

    init {
        loadVentas()
        loadEstadisticas()
    }

    fun onEvent(event: AdminVentasUiEvent) {
        when (event) {
            is AdminVentasUiEvent.LoadVentas -> loadVentas(
                pagina = event.pagina,
                tamanoPagina = event.tamanoPagina,
                fechaDesde = event.fechaDesde,
                fechaHasta = event.fechaHasta,
                usuarioId = event.usuarioId
            )
            is AdminVentasUiEvent.ShowDetalleVenta -> showDetalleVenta(event.venta)
            is AdminVentasUiEvent.CloseDetalleDialog -> closeDetalleDialog()
            is AdminVentasUiEvent.ClearError -> _state.update { it.copy(error = null) }
            is AdminVentasUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is AdminVentasUiEvent.OnFiltroOrdenChanged -> onFiltroOrdenChanged(event.filtro)
            is AdminVentasUiEvent.ChangePage -> changePage(event.pagina)
            is AdminVentasUiEvent.ApplyFilters -> applyFilters(event.fechaDesde, event.fechaHasta, event.usuarioId)
            is AdminVentasUiEvent.ClearFilters -> clearFilters()
            is AdminVentasUiEvent.LoadEstadisticas -> loadEstadisticas(event.fechaDesde, event.fechaHasta)
        }
    }

    private fun loadVentas(
        pagina: Int = 1,
        tamanoPagina: Int = 10,
        fechaDesde: String? = null,
        fechaHasta: String? = null,
        usuarioId: String? = null
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getAllVentasUseCase(pagina, tamanoPagina, fechaDesde, fechaHasta, usuarioId)) {
                is Resource.Success -> {
                    result.data?.let { ventasPaginadas ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                ventas = ventasPaginadas.ventas,
                                ventasFiltradas = ventasPaginadas.ventas,
                                paginaActual = ventasPaginadas.paginaActual,
                                totalPaginas = ventasPaginadas.totalPaginas,
                                totalVentas = ventasPaginadas.totalIngresos,
                                cantidadVentas = ventasPaginadas.totalVentas,
                                error = null
                            )
                        }
                        aplicarFiltros()
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar ventas"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun loadEstadisticas(
        fechaDesde: String? = null,
        fechaHasta: String? = null
    ) {
        viewModelScope.launch {
            when (val result = getEstadisticasVentasUseCase(fechaDesde, fechaHasta)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(estadisticas = result.data)
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun showDetalleVenta(venta: VentaDetallada) {
        _state.update {
            it.copy(
                ventaSeleccionada = venta,
                showDetalleDialog = true
            )
        }
    }

    private fun closeDetalleDialog() {
        _state.update {
            it.copy(
                ventaSeleccionada = null,
                showDetalleDialog = false
            )
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }

    private fun onFiltroOrdenChanged(filtro: FiltroOrdenVenta) {
        _state.update { it.copy(filtroOrden = filtro) }
        aplicarFiltros()
    }

    private fun changePage(pagina: Int) {
        val currentState = _state.value
        loadVentas(
            pagina = pagina,
            fechaDesde = currentState.fechaDesde,
            fechaHasta = currentState.fechaHasta,
            usuarioId = currentState.usuarioIdFiltro
        )
    }

    private fun applyFilters(fechaDesde: String?, fechaHasta: String?, usuarioId: String?) {
        _state.update {
            it.copy(
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta,
                usuarioIdFiltro = usuarioId
            )
        }
        loadVentas(fechaDesde = fechaDesde, fechaHasta = fechaHasta, usuarioId = usuarioId)
        loadEstadisticas(fechaDesde = fechaDesde, fechaHasta = fechaHasta)
    }

    private fun clearFilters() {
        _state.update {
            it.copy(
                fechaDesde = null,
                fechaHasta = null,
                usuarioIdFiltro = null,
                searchQuery = ""
            )
        }
        loadVentas()
        loadEstadisticas()
    }

    private fun aplicarFiltros() {
        val state = _state.value
        var ventasFiltradas = state.ventas

        if (state.searchQuery.isNotBlank()) {
            ventasFiltradas = ventasFiltradas.filter { venta ->
                venta.pago.nombreTitular.contains(state.searchQuery, ignoreCase = true) ||
                venta.ventaId.toString().contains(state.searchQuery) ||
                venta.pago.direccion.contains(state.searchQuery, ignoreCase = true)
            }
        }
        
        ventasFiltradas = when (state.filtroOrden) {
            FiltroOrdenVenta.MAS_RECIENTE -> ventasFiltradas.sortedByDescending { it.fecha }
            FiltroOrdenVenta.MAS_ANTIGUA -> ventasFiltradas.sortedBy { it.fecha }
            FiltroOrdenVenta.MAYOR_MONTO -> ventasFiltradas.sortedByDescending { it.total }
            FiltroOrdenVenta.MENOR_MONTO -> ventasFiltradas.sortedBy { it.total }
        }

        _state.update { it.copy(ventasFiltradas = ventasFiltradas) }
    }
}