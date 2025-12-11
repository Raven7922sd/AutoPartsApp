package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Venta
import com.autoparts.dominio.repository.VentasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVentasUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    operator fun invoke(): Flow<List<Venta>> = flow {
        when (val result = repository.getVentas()) {
            is Resource.Success -> emit(result.data ?: emptyList())
            is Resource.Error -> emit(emptyList())
            is Resource.Loading -> emit(emptyList())
        }
    }
}