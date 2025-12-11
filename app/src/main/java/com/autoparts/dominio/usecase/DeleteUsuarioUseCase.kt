package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.repository.UsuarioRepository
import javax.inject.Inject

/**
 * Caso de uso para eliminar un usuario.
 * Requiere permisos de Admin en la API.
 */
class DeleteUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        return repository.deleteUser(id)
    }
}

