package com.autoparts.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CreateVenta
import com.autoparts.dominio.usecase.ProcessCheckoutUseCase
import com.autoparts.dominio.usecase.ClearCarritoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val clearCarritoUseCase: ClearCarritoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutUiState())
    val state: StateFlow<CheckoutUiState> = _state.asStateFlow()

    fun onEvent(event: CheckoutUiEvent) {
        when (event) {
            is CheckoutUiEvent.NombreTitularChanged -> {
                _state.update {
                    it.copy(
                        nombreTitular = event.nombre,
                        nombreTitularError = null
                    )
                }
            }
            is CheckoutUiEvent.NumeroTarjetaChanged -> {
                val numero = event.numero.filter { it.isDigit() }.take(16)
                _state.update {
                    it.copy(
                        numeroTarjeta = numero,
                        numeroTarjetaError = null
                    )
                }
            }
            is CheckoutUiEvent.FechaExpiracionChanged -> {
                val fecha = formatearFecha(event.fecha)
                _state.update {
                    it.copy(
                        fechaExpiracion = fecha,
                        fechaExpiracionError = null
                    )
                }
            }
            is CheckoutUiEvent.CvvChanged -> {
                val cvv = event.cvv.filter { it.isDigit() }.take(4)
                _state.update {
                    it.copy(
                        cvv = cvv,
                        cvvError = null
                    )
                }
            }
            is CheckoutUiEvent.DireccionChanged -> {
                _state.update {
                    it.copy(
                        direccion = event.direccion,
                        direccionError = null
                    )
                }
            }
            is CheckoutUiEvent.ProcessCheckout -> processCheckout()
            is CheckoutUiEvent.UserMessageShown -> {
                _state.update { it.copy(userMessage = null) }
            }
            is CheckoutUiEvent.DismissSuccess -> {
                _state.update { it.copy(checkoutSuccess = false, venta = null) }
            }
        }
    }

    private fun formatearFecha(input: String): String {
        val numeros = input.filter { it.isDigit() }
        return when {
            numeros.length <= 2 -> numeros
            numeros.length <= 4 -> "${numeros.substring(0, 2)}/${numeros.substring(2)}"
            else -> "${numeros.substring(0, 2)}/${numeros.substring(2, 4)}"
        }
    }

    private fun processCheckout() {
        var hasErrors = false

        if (_state.value.nombreTitular.isBlank()) {
            _state.update { it.copy(nombreTitularError = "Campo requerido") }
            hasErrors = true
        }

        if (_state.value.numeroTarjeta.length < 13) {
            _state.update { it.copy(numeroTarjetaError = "Número de tarjeta inválido") }
            hasErrors = true
        }

        if (!_state.value.fechaExpiracion.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))) {
            _state.update { it.copy(fechaExpiracionError = "Formato: MM/AA") }
            hasErrors = true
        }

        if (_state.value.cvv.length < 3) {
            _state.update { it.copy(cvvError = "CVV inválido") }
            hasErrors = true
        }

        if (_state.value.direccion.isBlank()) {
            _state.update { it.copy(direccionError = "Campo requerido") }
            hasErrors = true
        }

        if (hasErrors) return

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }

            val createVenta = CreateVenta(
                nombreTitular = _state.value.nombreTitular,
                numeroTarjeta = _state.value.numeroTarjeta,
                fechaExpiracion = _state.value.fechaExpiracion,
                cvv = _state.value.cvv,
                direccion = _state.value.direccion
            )

            when (val result = processCheckoutUseCase(createVenta)) {
                is Resource.Success -> {
                    result.data?.let { venta ->
                        _state.update {
                            it.copy(
                                venta = venta,
                                checkoutSuccess = true,
                                isProcessing = false,
                                nombreTitular = "",
                                numeroTarjeta = "",
                                fechaExpiracion = "",
                                cvv = "",
                                direccion = ""
                            )
                        }

                        try {
                            clearCarritoUseCase()
                        } catch (e: Exception) {
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            userMessage = result.message ?: "Error al procesar la compra",
                            isProcessing = false
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}