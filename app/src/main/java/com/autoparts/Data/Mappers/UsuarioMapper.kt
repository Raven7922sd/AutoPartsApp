package com.autoparts.Data.Mappers

import com.autoparts.Data.Remote.Dto.UsuariosDto
import com.autoparts.dominio.model.Usuarios

fun Usuarios.toDto() : UsuariosDto = UsuariosDto(
    usuarioId =  usuarioId,
    userName = userName,
    password = password
)

fun UsuariosDto.toDomain(): Usuarios = Usuarios(
    usuarioId = usuarioId,
    userName = userName,
    password = password
)