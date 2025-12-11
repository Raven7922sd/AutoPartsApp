package com.autoparts.dominio.repository

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.model.CarritoTotal
import com.autoparts.dominio.model.AddCarrito
import com.autoparts.dominio.model.UpdateCarrito

interface CarritoRepository {
    suspend fun getCarrito(): Resource<List<Carrito>>
    suspend fun getCarritoTotal(): Resource<CarritoTotal>
    suspend fun addItem(addCarrito: AddCarrito): Resource<Carrito>
    suspend fun updateItem(carritoId: Int, updateCarrito: UpdateCarrito): Resource<Unit>
    suspend fun deleteItem(carritoId: Int): Resource<Unit>
    suspend fun clearCarrito(): Resource<Unit>
}

