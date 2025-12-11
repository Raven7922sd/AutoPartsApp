package com.autoparts.domain.usecase.usuario

import com.autoparts.domain.repository.UsuarioRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}