package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CreateUser
import com.autoparts.dominio.model.Usuarios
import com.autoparts.dominio.repository.UsuarioRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(email: String, password: String, phoneNumber: String? = null): Resource<Usuarios> {
        val createUser = CreateUser(email = email, password = password, phoneNumber = phoneNumber)
        return repository.register(createUser)
    }
}

