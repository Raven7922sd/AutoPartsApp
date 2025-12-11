package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.usuario.UsuarioDto
import com.autoparts.domain.model.Usuarios

object UsuarioMapper {
    fun toDomain(dto: UsuarioDto): Usuarios {
        return Usuarios(
            id = dto.id ?: dto.email,
            userName = dto.userName,
            email = dto.email,
            phoneNumber = dto.phoneNumber,
            emailConfirmed = dto.emailConfirmed,
            roles = dto.roles
        )
    }
}