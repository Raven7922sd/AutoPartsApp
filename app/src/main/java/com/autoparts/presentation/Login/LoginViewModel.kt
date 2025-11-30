package com.autoparts.presentation.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import com.autoparts.dominio.usecase.LoginUseCase
import com.autoparts.dominio.usecase.RegisterUseCase
import com.autoparts.presentation.validation.UsuarioValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
    private val validator: UsuarioValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Efecto>()
    val effects: SharedFlow<Efecto> = _effects

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.emailChanged -> _state.update { it.copy(email = event.email) }
            is LoginUiEvent.passwordChanged -> _state.update { it.copy(password = event.password) }
            is LoginUiEvent.phoneNumberChanged -> _state.update { it.copy(phoneNumber = event.phoneNumber) }
            is LoginUiEvent.loginModeClicked -> _state.update { it.copy(isRegistering = false) }
            is LoginUiEvent.submitLogin -> onSubmitLogin()
            is LoginUiEvent.registerModeClicked -> onRegisterModeClicked()
            is LoginUiEvent.submitRegistration -> onSubmitRegistration()
            is LoginUiEvent.userMessageShown -> _state.update { it.copy(userMessage = "") }
        }
    }

    private fun onSubmitLogin() {
        viewModelScope.launch {
            val emailVal = validator.validateEmail(_state.value.email)
            val passVal = validator.validatePassword(_state.value.password)

            _state.update {
                it.copy(
                    emailError = if (!emailVal.isValid) emailVal.errorMessage else null,
                    passwordError = if (!passVal.isValid) passVal.errorMessage else null
                )
            }

            if (!emailVal.isValid || !passVal.isValid) {
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            when (val result = loginUseCase(_state.value.email, _state.value.password)) {
                is Resource.Success -> {
                    val loginResult = result.data
                    if (loginResult != null) {
                        sessionManager.saveSession(
                            userId = loginResult.email,
                            email = loginResult.email,
                            userName = loginResult.email.substringBefore("@"),
                            jwtToken = loginResult.accessToken,
                            refreshToken = loginResult.refreshToken
                        )

                        _state.update {
                            it.copy(
                                userId = loginResult.email,
                                isLoading = false,
                                userMessage = "¡Bienvenido!"
                            )
                        }
                        _effects.emit(Efecto.NavigateHome(loginResult.email))
                    } else {
                        _state.update {
                            it.copy(
                                userMessage = "Error al iniciar sesión",
                                isLoading = false
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            userMessage = result.message ?: "Email o contraseña incorrectos",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun onSubmitRegistration() {
        viewModelScope.launch {
            val emailVal = validator.validateEmail(_state.value.email)
            val passVal = validator.validatePassword(_state.value.password)
            val phoneVal = validator.validatePhoneNumber(_state.value.phoneNumber.ifBlank { null })

            _state.update {
                it.copy(
                    emailError = if (!emailVal.isValid) emailVal.errorMessage else null,
                    passwordError = if (!passVal.isValid) passVal.errorMessage else null,
                    phoneNumberError = if (!phoneVal.isValid) phoneVal.errorMessage else null
                )
            }

            if (!emailVal.isValid || !passVal.isValid || !phoneVal.isValid) {
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val phoneNumber = _state.value.phoneNumber.ifBlank { null }

            when (val result = registerUseCase(_state.value.email, _state.value.password, phoneNumber)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            email = "",
                            password = "",
                            phoneNumber = "",
                            emailError = null,
                            passwordError = null,
                            phoneNumberError = null,
                            isRegistering = false,
                            isLoading = false,
                            userMessage = "Usuario registrado correctamente. Por favor inicia sesión."
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            userMessage = result.message ?: "Error al registrar usuario",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun onRegisterModeClicked() {
        _state.update {
            it.copy(
                email = "",
                password = "",
                phoneNumber = "",
                emailError = null,
                passwordError = null,
                phoneNumberError = null,
                isRegistering = !it.isRegistering,
                isLoading = false,
                userMessage = ""
            )
        }
    }
}