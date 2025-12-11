package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.UpdateUser
import com.autoparts.dominio.repository.UsuarioRepository
import javax.inject.Inject

class UpdateUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(
        id: String,
        email: String? = null,
        phoneNumber: String? = null,
        currentPassword: String? = null,
        newPassword: String? = null
    ): Resource<Unit> {
        val updateUser = UpdateUser(
            email = email,
            phoneNumber = phoneNumber,
            currentPassword = currentPassword,
            newPassword = newPassword
        )
        return repository.updateUser(id, updateUser)
    }
}
