package com.autoparts.domain.usecase.usuario

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateUser
import com.autoparts.domain.model.Usuarios
import com.autoparts.domain.repository.UsuarioRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(createUser: CreateUser): Resource<Usuarios> {
        return repository.register(createUser)
    }
}


