package com.autoparts.presentation.productodetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.local.CarritoLocalManager
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.dominio.model.Producto
import com.autoparts.dominio.usecase.AddCarritoItemUseCase
import com.autoparts.dominio.usecase.GetProductoUseCase
import com.autoparts.dominio.usecase.SaveProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoDetalleViewModel @Inject constructor(
    private val getProductoUseCase: GetProductoUseCase,
    private val saveProductoUseCase: SaveProductoUseCase,
    private val addCarritoItemUseCase: AddCarritoItemUseCase,
    private val carritoLocalManager: CarritoLocalManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProductoDetalleUiState())
    val state: StateFlow<ProductoDetalleUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProductoDetalleEfecto>()
    val effects: SharedFlow<ProductoDetalleEfecto> = _effects

    fun onEvent(event: ProductoDetalleUiEvent) {
        when (event) {
            is ProductoDetalleUiEvent.LoadProducto -> loadProducto(event.productoId)
            is ProductoDetalleUiEvent.NombreChanged -> _state.update {
                it.copy(productoNombre = event.nombre, productoNombreError = null)
            }
            is ProductoDetalleUiEvent.MontoChanged -> _state.update {
                it.copy(productoMonto = event.monto, productoMontoError = null)
            }
            is ProductoDetalleUiEvent.CantidadChanged -> _state.update {
                it.copy(productoCantidad = event.cantidad, productoCantidadError = null)
            }
            is ProductoDetalleUiEvent.DescripcionChanged -> _state.update {
                it.copy(productoDescripcion = event.descripcion, productoDescripcionError = null)
            }
            is ProductoDetalleUiEvent.CategoriaChanged -> _state.update {
                it.copy(categoria = event.categoria, categoriaError = null)
            }
            is ProductoDetalleUiEvent.Save -> onSave()
            is ProductoDetalleUiEvent.ToggleEditMode -> _state.update {
                it.copy(isEditMode = !it.isEditMode)
            }
            is ProductoDetalleUiEvent.UserMessageShown -> _state.update {
                it.copy(userMessage = null)
            }
            is ProductoDetalleUiEvent.AddToCarrito -> addToCarrito(event.cantidad)
        }
    }

    private fun loadProducto(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, productoId = id) }

            when (val result = getProductoUseCase(id)) {
                is Resource.Success -> {
                    result.data?.let { producto ->
                        _state.update {
                            it.copy(
                                productoNombre = producto.productoNombre,
                                productoMonto = producto.productoMonto.toString(),
                                productoCantidad = producto.productoCantidad.toString(),
                                productoDescripcion = producto.productoDescripcion,
                                categoria = producto.categoria,
                                productoImagenUrl = producto.productoImagenUrl,
                                fecha = producto.fecha,
                                isLoading = false
                            )
                        }
                    } ?: _state.update { it.copy(userMessage = "Producto no encontrado", isLoading = false) }
                }
                is Resource.Error -> _state.update { it.copy(userMessage = result.message ?: "Error cargando producto", isLoading = false) }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            var hasErrors = false

            if (_state.value.productoNombre.isBlank()) {
                _state.update { it.copy(productoNombreError = "El nombre es requerido") }
                hasErrors = true
            }

            val monto = _state.value.productoMonto.toIntOrNull()
            if (monto == null || monto <= 0) {
                _state.update { it.copy(productoMontoError = "El monto debe ser mayor a 0") }
                hasErrors = true
            }

            val cantidad = _state.value.productoCantidad.toIntOrNull()
            if (cantidad == null || cantidad < 0) {
                _state.update { it.copy(productoCantidadError = "La cantidad debe ser válida") }
                hasErrors = true
            }

            if (_state.value.categoria.isBlank()) {
                _state.update { it.copy(categoriaError = "La categoría es requerida") }
                hasErrors = true
            }

            if (hasErrors) return@launch

            _state.update { it.copy(isLoading = true) }

            val producto = Producto(
                productoId = _state.value.productoId,
                productoNombre = _state.value.productoNombre,
                productoMonto = monto!!,
                productoCantidad = cantidad!!,
                productoDescripcion = _state.value.productoDescripcion,
                categoria = _state.value.categoria,
                productoImagenUrl = _state.value.productoImagenUrl,
                fecha = _state.value.fecha
            )

            when (val result = saveProductoUseCase(producto)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            userMessage = "Producto guardado correctamente",
                            isLoading = false,
                            isEditMode = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            userMessage = result.message ?: "Error al guardar producto",
                            isLoading = false
                        )
                    }
                }
                else -> {
                    _state.update {
                        it.copy(
                            userMessage = "Error desconocido",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun addToCarrito(cantidad: Int) {
        viewModelScope.launch {
            val productoId = _state.value.productoId ?: return@launch

            _state.update { it.copy(isAddingToCarrito = true) }

            val addCarrito = AddCarrito(
                productoId = productoId,
                cantidad = cantidad
            )

            when (val result = addCarritoItemUseCase(addCarrito)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            userMessage = "Producto agregado al carrito",
                            isAddingToCarrito = false
                        )
                    }
                }
                is Resource.Error -> {
                    val producto = Producto(
                        productoId = productoId,
                        productoNombre = _state.value.productoNombre,
                        productoMonto = _state.value.productoMonto.toIntOrNull() ?: 0,
                        productoCantidad = _state.value.productoCantidad.toIntOrNull() ?: 0,
                        productoDescripcion = _state.value.productoDescripcion,
                        productoImagenUrl = _state.value.productoImagenUrl,
                        categoria = _state.value.categoria,
                        fecha = _state.value.fecha
                    )
                    carritoLocalManager.addItem(producto, cantidad)
                    _state.update {
                        it.copy(
                            userMessage = "Producto agregado al carrito",
                            isAddingToCarrito = false
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}