package com.autoparts.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.local.SessionManager
import com.autoparts.dominio.usecase.GetUsuarioByEmailUseCase
import com.autoparts.dominio.usecase.UpdateUsuarioUseCase
import com.autoparts.presentation.validation.UsuarioValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val getUsuarioByEmailUseCase: GetUsuarioByEmailUseCase,
    private val updateUsuarioUseCase: UpdateUsuarioUseCase,
    private val sessionManager: SessionManager,
    private val validator: UsuarioValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: PerfilUiEvent) {
        when (event) {
            is PerfilUiEvent.LoadProfile -> loadProfile()
            is PerfilUiEvent.UserNameChanged -> {
                _uiState.update { it.copy(userName = event.userName, userNameError = null) }
            }
            is PerfilUiEvent.PhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = event.phoneNumber, phoneNumberError = null) }
            }
            PerfilUiEvent.ShowEditDialog -> {
                _uiState.update { it.copy(showEditDialog = true) }
            }
            PerfilUiEvent.HideEditDialog -> {
                _uiState.update { it.copy(showEditDialog = false) }
            }
            PerfilUiEvent.SaveProfile -> saveProfile()
            PerfilUiEvent.ClearMessages -> {
                _uiState.update { it.copy(successMessage = "", errorMessage = "") }
            }
            PerfilUiEvent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _uiState.update { PerfilUiState() }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val email = sessionManager.getUserEmail()
            if (email == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se encontró la sesión del usuario"
                    )
                }
                return@launch
            }

            when (val result = getUsuarioByEmailUseCase(email)) {
                is Resource.Success -> {
                    val usuario = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            usuario = usuario,
                            email = usuario?.email ?: "",
                            userName = usuario?.userName ?: "",
                            phoneNumber = usuario?.phoneNumber ?: ""
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error al cargar el perfil"
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val userId = _uiState.value.usuario?.id
            if (userId == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo obtener el ID del usuario"
                    )
                }
                return@launch
            }

            val userNameValidation = validator.validateUserName(_uiState.value.userName)
            val phoneValidation = validator.validatePhoneNumber(
                _uiState.value.phoneNumber.ifBlank { null }
            )

            if (!userNameValidation.isValid || !phoneValidation.isValid) {
                _uiState.update {
                    it.copy(
                        userNameError = if (!userNameValidation.isValid) userNameValidation.errorMessage else null,
                        phoneNumberError = if (!phoneValidation.isValid) phoneValidation.errorMessage else null
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            when (val result = updateUsuarioUseCase(
                id = userId,
                email = null,
                phoneNumber = _uiState.value.phoneNumber.ifBlank { null },
                currentPassword = null,
                newPassword = null
            )) {
                is Resource.Success -> {
                    val email = sessionManager.getUserEmail() ?: ""
                    sessionManager.saveSession(
                        userId = email,
                        email = email,
                        userName = _uiState.value.userName,
                        jwtToken = sessionManager.getJwtToken() ?: "",
                        refreshToken = sessionManager.getRefreshToken() ?: ""
                    )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showEditDialog = false,
                            successMessage = "Perfil actualizado correctamente"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error al actualizar el perfil"
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}