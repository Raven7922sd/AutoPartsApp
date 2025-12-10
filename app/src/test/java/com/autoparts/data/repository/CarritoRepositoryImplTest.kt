package com.autoparts.data.repository

import com.autoparts.data.local.manager.CarritoLocalManager
import com.autoparts.data.remote.datasource.CarritoRemoteDataSource
import com.autoparts.data.remote.dto.carrito.AddCarritoRequest
import com.autoparts.data.remote.dto.carrito.CarritoDto
import com.autoparts.data.remote.dto.carrito.CarritoTotalResponse
import com.autoparts.data.remote.dto.carrito.UpdateCarritoRequest
import com.autoparts.data.remote.dto.producto.ProductoDto
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.AddCarrito
import com.autoparts.domain.model.Carrito
import com.autoparts.domain.model.UpdateCarrito
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class CarritoRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: CarritoRemoteDataSource

    @Mock
    private lateinit var carritoLocalManager: CarritoLocalManager

    private lateinit var repository: CarritoRepositoryImpl

    private val mockCarritoDto = CarritoDto(
        carritoId = 1,
        applicationUserId = "user123",
        productoId = 1,
        producto = ProductoDto(
            productoId = 1,
            productoNombre = "Producto Test",
            productoMonto = 100,
            productoCantidad = 10,
            productoDescripcion = "Descripción test",
            productoImagenUrl = "url",
            categoria = "Test",
            fecha = "2025-12-06"
        ),
        cantidad = 2
    )

    private val mockCarritoTotalResponse = CarritoTotalResponse(
        totalItems = 5,
        totalPrice = 500.0
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CarritoRepositoryImpl(remoteDataSource, carritoLocalManager)
    }

    @Test
    fun `getCarrito returns success when remote data source succeeds`() = runTest {
        val carritoList = listOf(mockCarritoDto)
        whenever(remoteDataSource.getCarrito()).thenReturn(Resource.Success(carritoList))

        val result = repository.getCarrito()

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        assertEquals(1, result.data?.first()?.carritoId)
        verify(remoteDataSource).getCarrito()
    }

    @Test
    fun `getCarrito returns error when remote data source fails and local is empty`() = runTest {
        whenever(remoteDataSource.getCarrito()).thenReturn(Resource.Error("Network error"))
        whenever(carritoLocalManager.getItems()).thenReturn(flowOf(emptyList()))

        val result = repository.getCarrito()

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
        verify(remoteDataSource).getCarrito()
        verify(carritoLocalManager).getItems()
    }

    @Test
    fun `getCarrito returns success with empty list when remote fails but local has items`() = runTest {
        val localItems = listOf(AddCarrito(productoId = 1, cantidad = 2))
        whenever(remoteDataSource.getCarrito()).thenReturn(Resource.Error("Network error"))
        whenever(carritoLocalManager.getItems()).thenReturn(flowOf(localItems))

        val result = repository.getCarrito()

        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data?.isEmpty() == true)
    }

    @Test
    fun `getCarritoTotal returns success when remote data source succeeds`() = runTest {
        whenever(remoteDataSource.getCarritoTotal()).thenReturn(Resource.Success(mockCarritoTotalResponse))

        val result = repository.getCarritoTotal()

        assertTrue(result is Resource.Success)
        assertEquals(5, (result as Resource.Success).data?.totalItems)
        assertEquals(500.0, result.data?.totalPrice ?: 0.0, 0.01)
        verify(remoteDataSource).getCarritoTotal()
    }

    @Test
    fun `getCarritoTotal returns error when remote data source fails`() = runTest {
        whenever(remoteDataSource.getCarritoTotal()).thenReturn(Resource.Error("Network error"))

        val result = repository.getCarritoTotal()

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
        verify(remoteDataSource).getCarritoTotal()
    }

    @Test
    fun `addCarritoItem adds to local manager and calls remote data source`() = runTest {
        val addCarrito = AddCarrito(productoId = 1, cantidad = 2)
        val request = AddCarritoRequest(productoId = 1, cantidad = 2)
        whenever(remoteDataSource.addCarritoItem(any())).thenReturn(Resource.Success(mockCarritoDto))

        val result = repository.addCarritoItem(addCarrito)

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.carritoId)
        verify(carritoLocalManager).addItem(addCarrito)
        verify(remoteDataSource).addCarritoItem(any())
    }

    @Test
    fun `addCarritoItem returns local carrito when remote fails`() = runTest {
        val addCarrito = AddCarrito(productoId = 1, cantidad = 2)
        whenever(remoteDataSource.addCarritoItem(any())).thenReturn(Resource.Error("Network error"))

        val result = repository.addCarritoItem(addCarrito)

        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.carritoId)
        assertEquals(1, result.data?.productoId)
        assertEquals(2, result.data?.cantidad)
        verify(carritoLocalManager).addItem(addCarrito)
    }

    @Test
    fun `updateCarritoItem calls remote data source with correct parameters`() = runTest {
        val carritoId = 1
        val updateCarrito = UpdateCarrito(cantidad = 3)
        val request = UpdateCarritoRequest(cantidad = 3)
        whenever(remoteDataSource.updateCarritoItem(carritoId, request)).thenReturn(Resource.Success(Unit))

        val result = repository.updateCarritoItem(carritoId, updateCarrito)

        assertTrue(result is Resource.Success)
        verify(remoteDataSource).updateCarritoItem(eq(carritoId), any())
    }

    @Test
    fun `deleteCarritoItem llama a la fuente de datos remota con el id correcto`() = runTest {
        val carritoId = 1
        whenever(remoteDataSource.deleteCarritoItem(carritoId)).thenReturn(Resource.Success(Unit))

        val result = repository.deleteCarritoItem(carritoId)

        assertTrue(result is Resource.Success)
        verify(remoteDataSource).deleteCarritoItem(carritoId)
    }

    @Test
    fun `clearCarrito limpia el gestor local y llama a la fuente de datos remota`() = runTest {
        whenever(remoteDataSource.clearCarrito()).thenReturn(Resource.Success(Unit))

        val result = repository.clearCarrito()

        assertTrue(result is Resource.Success)
        verify(carritoLocalManager).clearCarrito()
        verify(remoteDataSource).clearCarrito()
    }

    @Test
    fun `getCarrito retorna cargando cuando la fuente de datos remota está cargando`() = runTest {
        whenever(remoteDataSource.getCarrito()).thenReturn(Resource.Loading())

        val result = repository.getCarrito()

        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `getCarritoTotal retorna error cuando los datos son nulos`() = runTest {
        whenever(remoteDataSource.getCarritoTotal()).thenReturn(Resource.Success(null))

        val result = repository.getCarritoTotal()

        assertTrue(result is Resource.Error)
        assertEquals("Error al obtener total", (result as Resource.Error).message)
    }

    @Test
    fun `addCarritoItem retorna error cuando los datos remotos son nulos`() = runTest {
        val addCarrito = AddCarrito(productoId = 1, cantidad = 2)
        whenever(remoteDataSource.addCarritoItem(any())).thenReturn(Resource.Success(null))

        val result = repository.addCarritoItem(addCarrito)

        assertTrue(result is Resource.Error)
        assertEquals("Error al agregar item", (result as Resource.Error).message)
    }
}

