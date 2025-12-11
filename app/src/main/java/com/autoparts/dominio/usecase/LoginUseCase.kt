package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.LoginResult
import com.autoparts.dominio.model.LoginUser
import com.autoparts.dominio.repository.UsuarioRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<LoginResult> {
        val loginUser = LoginUser(email = email, password = password)
        return repository.login(loginUser)
    }
}