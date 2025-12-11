package com.autoparts.domain.usecase.usuario

import com.autoparts.domain.model.Usuarios
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUsuariosUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<Usuarios>> {
        return flowOf(emptyList())
    }
}