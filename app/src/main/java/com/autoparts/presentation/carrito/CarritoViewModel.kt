package com.autoparts.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.local.CarritoLocalManager
import com.autoparts.Data.local.SessionManager
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.dominio.model.CarritoTotal
import com.autoparts.dominio.model.UpdateCarrito
import com.autoparts.dominio.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val getCarritoUseCase: GetCarritoUseCase,
    private val getCarritoTotalUseCase: GetCarritoTotalUseCase,
    private val addCarritoItemUseCase: AddCarritoItemUseCase,
    private val updateCarritoItemUseCase: UpdateCarritoItemUseCase,
    private val deleteCarritoItemUseCase: DeleteCarritoItemUseCase,
    private val clearCarritoUseCase: ClearCarritoUseCase,
    private val carritoLocalManager: CarritoLocalManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    init {
        loadCarrito()
        viewModelScope.launch {
            carritoLocalManager.carritoItems.collect { localItems ->
                if (!_uiState.value.isUserLoggedIn) {
                    val (totalItems, totalPrice) = carritoLocalManager.getTotal()
                    _uiState.update {
                        it.copy(
                            carritoItems = localItems,
                            carritoTotal = CarritoTotal(totalItems, totalPrice)
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.LoadCarrito -> loadCarrito()
            is CarritoUiEvent.AddItem -> addItem(event.productoId, event.cantidad)
            is CarritoUiEvent.UpdateItem -> updateItem(event.carritoId, event.cantidad)
            is CarritoUiEvent.DeleteItem -> deleteItem(event.carritoId)
            is CarritoUiEvent.ClearCarrito -> clearCarrito()
            is CarritoUiEvent.ClearError -> clearError()
            is CarritoUiEvent.ClearSuccess -> clearSuccess()
        }
    }

    private fun loadCarrito() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val isLoggedIn = sessionManager.isLoggedIn()

            if (isLoggedIn) {
                when (val result = getCarritoUseCase()) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                carritoItems = result.data ?: emptyList(),
                                isLoading = false,
                                error = null,
                                isUserLoggedIn = true
                            )
                        }
                        loadTotal()
                    }
                    is Resource.Error -> {
                        val localItems = carritoLocalManager.carritoItems.value
                        val (totalItems, totalPrice) = carritoLocalManager.getTotal()
                        _uiState.update {
                            it.copy(
                                carritoItems = localItems,
                                carritoTotal = CarritoTotal(totalItems, totalPrice),
                                isLoading = false,
                                error = null,
                                isUserLoggedIn = true
                            )
                        }
                    }
                    is Resource.Loading -> {}
                }
            } else {
                val localItems = carritoLocalManager.carritoItems.value
                val (totalItems, totalPrice) = carritoLocalManager.getTotal()
                _uiState.update {
                    it.copy(
                        carritoItems = localItems,
                        carritoTotal = CarritoTotal(totalItems, totalPrice),
                        isLoading = false,
                        error = null,
                        isUserLoggedIn = false
                    )
                }
            }
        }
    }

    private fun loadTotal() {
        viewModelScope.launch {
            when (val result = getCarritoTotalUseCase()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(carritoTotal = result.data ?: it.carritoTotal)
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun addItem(productoId: Int, cantidad: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val addCarrito = AddCarrito(productoId, cantidad)
            when (val result = addCarritoItemUseCase(addCarrito)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Producto agregado al carrito"
                        )
                    }
                    loadCarrito()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun updateItem(carritoId: Int, cantidad: Int) {
        viewModelScope.launch {
            if (_uiState.value.isUserLoggedIn) {
                _uiState.update { it.copy(isLoading = true) }

                val updateCarrito = UpdateCarrito(cantidad)
                when (val result = updateCarritoItemUseCase(carritoId, updateCarrito)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = "Cantidad actualizada"
                            )
                        }
                        loadCarrito()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Resource.Loading -> {}
                }
            } else {
                carritoLocalManager.updateItem(carritoId, cantidad)
                val localItems = carritoLocalManager.carritoItems.value
                val (totalItems, totalPrice) = carritoLocalManager.getTotal()
                _uiState.update {
                    it.copy(
                        carritoItems = localItems,
                        carritoTotal = CarritoTotal(totalItems, totalPrice),
                        successMessage = "Cantidad actualizada"
                    )
                }
            }
        }
    }

    private fun deleteItem(carritoId: Int) {
        viewModelScope.launch {
            if (_uiState.value.isUserLoggedIn) {
                _uiState.update { it.copy(isLoading = true) }

                when (val result = deleteCarritoItemUseCase(carritoId)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = "Producto eliminado del carrito"
                            )
                        }
                        loadCarrito()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Resource.Loading -> {}
                }
            } else {
                carritoLocalManager.removeItem(carritoId)
                val localItems = carritoLocalManager.carritoItems.value
                val (totalItems, totalPrice) = carritoLocalManager.getTotal()
                _uiState.update {
                    it.copy(
                        carritoItems = localItems,
                        carritoTotal = CarritoTotal(totalItems, totalPrice),
                        successMessage = "Producto eliminado del carrito"
                    )
                }
            }
        }
    }

    private fun clearCarrito() {
        viewModelScope.launch {
            if (_uiState.value.isUserLoggedIn) {
                _uiState.update { it.copy(isLoading = true) }

                when (val result = clearCarritoUseCase()) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                carritoItems = emptyList(),
                                carritoTotal = CarritoTotal(),
                                isLoading = false,
                                successMessage = "Carrito vaciado"
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Resource.Loading -> {}
                }
            } else {
                carritoLocalManager.clearCarrito()
                _uiState.update {
                    it.copy(
                        carritoItems = emptyList(),
                        carritoTotal = CarritoTotal(),
                        successMessage = "Carrito vaciado"
                    )
                }
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun clearSuccess() {
        _uiState.update { it.copy(successMessage = null) }
    }
}