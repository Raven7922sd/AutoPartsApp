package com.autoparts.dominio.usecase

import com.autoparts.dominio.repository.UsuarioRepository
import javax.inject.Inject

class GetUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
){
    suspend operator fun invoke(id: Int) = repository.getUsuario(id)
}