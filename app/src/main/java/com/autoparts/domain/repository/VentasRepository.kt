package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateVenta
import com.autoparts.domain.model.EstadisticasVentas
import com.autoparts.domain.model.Venta
import com.autoparts.domain.model.VentasPaginadas

interface VentasRepository {
    suspend fun processCheckout(createVenta: CreateVenta): Resource<Venta>
    suspend fun getVentas(): Resource<List<Venta>>
    suspend fun getVenta(ventaId: Int): Resource<Venta>
    suspend fun getAllVentas(
        pagina: Int = 1,
        tamanoPagina: Int = 10,
        fechaDesde: String? = null,
        fechaHasta: String? = null,
        usuarioId: String? = null
    ): Resource<VentasPaginadas>
    suspend fun getEstadisticas(
        fechaDesde: String? = null,
        fechaHasta: String? = null
    ): Resource<EstadisticasVentas>
}