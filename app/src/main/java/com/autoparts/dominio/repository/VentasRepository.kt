package com.autoparts.dominio.repository

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CreateVenta
import com.autoparts.dominio.model.Venta

interface VentasRepository {
    suspend fun processCheckout(createVenta: CreateVenta): Resource<Venta>
    suspend fun getVentas(): Resource<List<Venta>>
    suspend fun getVenta(ventaId: Int): Resource<Venta>
}