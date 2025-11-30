package com.autoparts.presentation.Inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.local.CarritoLocalManager
import com.autoparts.Data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import com.autoparts.dominio.usecase.GetUsuarioUseCase
import com.autoparts.dominio.usecase.GetUsuarioByEmailUseCase
import com.autoparts.dominio.usecase.GetUsuariosUseCase
import com.autoparts.dominio.usecase.UpdateUsuarioUseCase
import com.autoparts.dominio.usecase.GetProductosUseCase
import com.autoparts.dominio.usecase.AddCarritoItemUseCase
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.presentation.validation.UsuarioValidator
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
class InicioViewModel @Inject constructor(
    private val getUsuarioUseCase: GetUsuarioUseCase,
    private val getUsuarioByEmailUseCase: GetUsuarioByEmailUseCase,
    private val getUsuariosUseCase: GetUsuariosUseCase,
    private val updateUsuarioUseCase: UpdateUsuarioUseCase,
    private val getProductosUseCase: GetProductosUseCase,
    private val addCarritoItemUseCase: AddCarritoItemUseCase,
    private val carritoLocalManager: CarritoLocalManager,
    private val sessionManager: SessionManager,
    private val validator: UsuarioValidator
) : ViewModel() {

    private val _state = MutableStateFlow(InicioUiState())
    val state: StateFlow<InicioUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Efecto>()
    val effects: SharedFlow<Efecto> = _effects

    init {
        loadProductos()
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionManager.userIdFlow.collect { userId ->
                if (userId != null) {
                    _state.update { it.copy(userId = userId) }
                    loadData(userId)
                } else {
                    _state.update { it.copy(userId = null, email = "", phoneNumber = "") }
                }
            }
        }
    }

    fun onEvent(event: InicioUiEvent) {
        when (event) {
            is InicioUiEvent.LoadUser -> loadData(event.userId)
            is InicioUiEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is InicioUiEvent.PhoneNumberChanged -> _state.update { it.copy(phoneNumber = event.phoneNumber) }
            is InicioUiEvent.SearchQueryChanged -> _state.update { it.copy(searchQuery = event.query) }
            is InicioUiEvent.CategorySelected -> _state.update { it.copy(selectedCategory = event.category) }
            is InicioUiEvent.AddToCarrito -> addToCarrito(event.productoId)
            is InicioUiEvent.LoadProductos -> loadProductos()
            is InicioUiEvent.Save -> onSave()
            is InicioUiEvent.Logout -> onLogout()
            is InicioUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
            is InicioUiEvent.showDialogEdit -> onShowDialogEdit()
            is InicioUiEvent.hideDialogEdit -> _state.update { it.copy(showDialog = false) }
        }
    }

    private fun loadProductos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingProductos = true) }
            try {
                val productos = getProductosUseCase().first()
                _state.update {
                    it.copy(
                        listProductos = productos,
                        isLoadingProductos = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        userMessage = "Error cargando productos: ${e.message}",
                        isLoadingProductos = false
                    )
                }
            }
        }
    }

    private fun loadData(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingUser = true, isLoadingList = true, userId = id) }

            val isEmail = id.contains("@")
            val userRes = if (isEmail) {
                getUsuarioByEmailUseCase(id)
            } else {
                getUsuarioUseCase(id)
            }

            when (userRes) {
                is Resource.Success -> {
                    val usuario = userRes.data
                    if (usuario != null) {
                        _state.update {
                            it.copy(
                                email = usuario.email,
                                phoneNumber = usuario.phoneNumber.orEmpty(),
                                isLoadingUser = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                userMessage = "Usuario no encontrado",
                                isLoadingUser = false
                            )
                        }
                    }
                }
                is Resource.Error -> _state.update {
                    it.copy(
                        userMessage = userRes.message ?: "Error cargando usuario",
                        isLoadingUser = false
                    )
                }
                is Resource.Loading -> _state.update { it.copy(isLoadingUser = true) }
            }

            try {
                val lista = getUsuariosUseCase().first()
                val others = lista.filter { u -> u.id != id }
                _state.update {
                    it.copy(
                        listUsuarios = others,
                        isLoadingList = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        userMessage = "Error cargando lista: ${e.message}",
                        isLoadingList = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val emailVal = validator.validateEmail(_state.value.email)
            val phoneVal = validator.validatePhoneNumber(_state.value.phoneNumber.ifBlank { null })

            _state.update {
                it.copy(
                    emailError = if (!emailVal.isValid) emailVal.errorMessage else null,
                    phoneNumberError = if (!phoneVal.isValid) phoneVal.errorMessage else null
                )
            }

            if (emailVal.isValid && phoneVal.isValid) {
                val id = _state.value.userId ?: return@launch
                _state.update { it.copy(isLoadingUser = true) }

                val phoneNumber = _state.value.phoneNumber.ifBlank { null }

                when (val res = updateUsuarioUseCase(
                    id = id,
                    email = _state.value.email,
                    phoneNumber = phoneNumber
                )) {
                    is Resource.Success -> _state.update {
                        it.copy(
                            userMessage = "Usuario actualizado correctamente",
                            isLoadingUser = false,
                            showDialog = false,
                            emailError = null,
                            phoneNumberError = null
                        )
                    }
                    is Resource.Error -> _state.update {
                        it.copy(
                            userMessage = res.message ?: "Error al actualizar usuario",
                            isLoadingUser = false
                        )
                    }
                    is Resource.Loading -> _state.update {
                        it.copy(isLoadingUser = true)
                    }
                }
            }
        }
    }

    private fun onShowDialogEdit() {
        _state.update {
            it.copy(
                showDialog = true,
                emailError = null,
                phoneNumberError = null
            )
        }
    }

    private fun onLogout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            carritoLocalManager.clearCarrito()
            _state.update {
                it.copy(
                    userId = null,
                    email = "",
                    phoneNumber = "",
                    listUsuarios = emptyList()
                )
            }
            _effects.emit(Efecto.NavigateLogin)
        }
    }

    private fun addToCarrito(productoId: Int) {
        viewModelScope.launch {
            val producto = _state.value.listProductos.find { it.productoId == productoId }
            if (producto == null) {
                _state.update {
                    it.copy(userMessage = "Producto no encontrado")
                }
                return@launch
            }

            if (_state.value.userId != null) {
                val addCarrito = AddCarrito(productoId = productoId, cantidad = 1)
                when (val result = addCarritoItemUseCase(addCarrito)) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(userMessage = "Producto agregado al carrito")
                        }
                    }
                    is Resource.Error -> {
                        carritoLocalManager.addItem(producto, 1)
                        _state.update {
                            it.copy(userMessage = "Producto agregado al carrito local")
                        }
                    }
                    is Resource.Loading -> {
                    }
                }
            } else {
                carritoLocalManager.addItem(producto, 1)
                _state.update {
                    it.copy(userMessage = "Producto agregado al carrito")
                }
            }
        }
    }
}