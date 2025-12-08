package com.autoparts.domain.usecase.usuario

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.UpdateUser
import com.autoparts.domain.repository.UsuarioRepository
import javax.inject.Inject

class UpdateUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: String, updateUser: UpdateUser): Resource<Unit> {
        return repository.updateUsuario(id, updateUser)
    }
}