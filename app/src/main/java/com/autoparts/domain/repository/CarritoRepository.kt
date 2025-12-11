package com.autoparts.domain.repository

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Carrito
import com.autoparts.domain.model.CarritoTotal
import com.autoparts.domain.model.AddCarrito
import com.autoparts.domain.model.UpdateCarrito

interface CarritoRepository {
    suspend fun getCarrito(): Resource<List<Carrito>>
    suspend fun getCarritoTotal(): Resource<CarritoTotal>
    suspend fun addCarritoItem(addCarrito: AddCarrito): Resource<Carrito>
    suspend fun updateCarritoItem(carritoId: Int, updateCarrito: UpdateCarrito): Resource<Unit>
    suspend fun deleteCarritoItem(carritoId: Int): Resource<Unit>
    suspend fun clearCarrito(): Resource<Unit>
}