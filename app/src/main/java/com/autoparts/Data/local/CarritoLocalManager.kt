package com.autoparts.Data.local

import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarritoLocalManager @Inject constructor() {

    private val _carritoItems = MutableStateFlow<List<Carrito>>(emptyList())
    val carritoItems: StateFlow<List<Carrito>> = _carritoItems.asStateFlow()

    fun addItem(producto: Producto, cantidad: Int = 1) {
        val currentItems = _carritoItems.value.toMutableList()
        val existingItem = currentItems.find { it.productoId == producto.productoId }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            val newCantidad = existingItem.cantidad + cantidad
            currentItems[index] = existingItem.copy(cantidad = newCantidad)
        } else {
            val newItem = Carrito(
                carritoId = currentItems.size + 1,
                applicationUserId = "",
                productoId = producto.productoId ?: 0,
                producto = producto,
                cantidad = cantidad
            )
            currentItems.add(newItem)
        }

        _carritoItems.value = currentItems
    }

    fun updateItem(carritoId: Int, cantidad: Int) {
        val currentItems = _carritoItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.carritoId == carritoId }

        if (index != -1) {
            currentItems[index] = currentItems[index].copy(cantidad = cantidad)
            _carritoItems.value = currentItems
        }
    }

    fun removeItem(carritoId: Int) {
        val currentItems = _carritoItems.value.toMutableList()
        currentItems.removeAll { it.carritoId == carritoId }
        _carritoItems.value = currentItems
    }

    fun clearCarrito() {
        _carritoItems.value = emptyList()
    }

    fun getTotal(): Pair<Int, Double> {
        val items = _carritoItems.value
        val totalItems = items.sumOf { it.cantidad }
        val totalPrice = items.sumOf { it.cantidad * (it.producto?.productoMonto?.toDouble() ?: 0.0) }
        return Pair(totalItems, totalPrice)
    }

    fun getItemCount(): Int {
        return _carritoItems.value.sumOf { it.cantidad }
    }
}