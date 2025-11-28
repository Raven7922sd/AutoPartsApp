package com.autoparts.presentation.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.dominio.usecase.GetProductosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductosViewModel @Inject constructor(
    private val getProductosUseCase: GetProductosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductosUiState())
    val state: StateFlow<ProductosUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProductosEfecto>()
    val effects: SharedFlow<ProductosEfecto> = _effects

    init {
        loadProductos()
    }

    fun onEvent(event: ProductosUiEvent) {
        when (event) {
            is ProductosUiEvent.LoadProductos -> loadProductos()
            is ProductosUiEvent.SearchQueryChanged -> _state.update {
                it.copy(searchQuery = event.query)
            }
            is ProductosUiEvent.UserMessageShown -> _state.update {
                it.copy(userMessage = null)
            }
        }
    }

    private fun loadProductos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val productos = getProductosUseCase().first()
                _state.update {
                    it.copy(
                        listProductos = productos,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        userMessage = "Error cargando productos: ${e.message}",
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun navigateToDetalle(productoId: Int) {
        viewModelScope.launch {
            _effects.emit(ProductosEfecto.NavigateToDetalle(productoId))
        }
    }
}

