package com.autoparts.domain.usecase.usuario

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Usuarios
import com.autoparts.domain.repository.UsuarioRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<Usuarios> {
        return repository.login(email, password)
    }
}