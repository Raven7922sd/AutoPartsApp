package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.VentasRemoteDataSource
import com.autoparts.data.remote.dto.venta.CheckoutRequest
import com.autoparts.data.remote.dto.venta.PagoInfoDto
import com.autoparts.data.remote.dto.venta.VentaDto
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateVenta
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class VentasRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: VentasRemoteDataSource

    private lateinit var repository: VentasRepositoryImpl

    private val mockPagoInfoDto = PagoInfoDto(
        pagoId = 1,
        nombreTitular = "Johan Pérez",
        numeroTarjetaEnmascarado = "****3456",
        direccion = "Calle Principal 123"
    )

    private val mockVentaDto = VentaDto(
        ventaId = 1,
        applicationUserId = "user123",
        total = 1500.0,
        fecha = "2025-12-06T10:00:00",
        detalles = emptyList(),
        pago = mockPagoInfoDto
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = VentasRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `processCheckout returns success when remote data source succeeds`() = runTest {
        val createVenta = CreateVenta(
            nombreTitular = "Johan Pérez",
            numeroTarjeta = "1234567890123456",
            fechaExpiracion = "12/25",
            cvv = "123",
            direccion = "Calle Principal 123"
        )
        whenever(remoteDataSource.processCheckout(any())).thenReturn(Resource.Success(mockVentaDto))

        val result = repository.processCheckout(createVenta)

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.ventaId)
        assertEquals(1500.0, result.data?.total ?: 0.0, 0.01)
        verify(remoteDataSource).processCheckout(any())
    }

    @Test
    fun `processCheckout returns error when data is null`() = runTest {
        val createVenta = CreateVenta(
            nombreTitular = "Johan Pérez",
            numeroTarjeta = "1234567890123456",
            fechaExpiracion = "12/25",
            cvv = "123",
            direccion = "Calle Principal 123"
        )
        whenever(remoteDataSource.processCheckout(any())).thenReturn(Resource.Success(null))

        val result = repository.processCheckout(createVenta)

        assertTrue(result is Resource.Error)
        assertEquals("Error al procesar compra", (result as Resource.Error).message)
    }

    @Test
    fun `processCheckout returns error when remote data source fails`() = runTest {
        val createVenta = CreateVenta(
            nombreTitular = "Johan Pérez",
            numeroTarjeta = "1234567890123456",
            fechaExpiracion = "12/25",
            cvv = "123",
            direccion = "Calle Principal 123"
        )
        whenever(remoteDataSource.processCheckout(any())).thenReturn(Resource.Error("Payment failed"))

        val result = repository.processCheckout(createVenta)

        assertTrue(result is Resource.Error)
        assertEquals("Payment failed", (result as Resource.Error).message)
    }

    @Test
    fun `processCheckout returns loading when remote data source is loading`() = runTest {
        val createVenta = CreateVenta(
            nombreTitular = "Johan Pérez",
            numeroTarjeta = "1234567890123456",
            fechaExpiracion = "12/25",
            cvv = "123",
            direccion = "Calle Principal 123"
        )
        whenever(remoteDataSource.processCheckout(any())).thenReturn(Resource.Loading())

        val result = repository.processCheckout(createVenta)

        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `processCheckout maps domain model to request correctly`() = runTest {
        val createVenta = CreateVenta(
            nombreTitular = "María López",
            numeroTarjeta = "9876543210987654",
            fechaExpiracion = "06/26",
            cvv = "456",
            direccion = "Av. Central 456"
        )
        whenever(remoteDataSource.processCheckout(any())).thenReturn(Resource.Success(mockVentaDto))

        repository.processCheckout(createVenta)

        verify(remoteDataSource).processCheckout(
            argThat { request ->
                request.pago.nombreTitular == "María López" &&
                request.pago.numeroTarjeta == "9876543210987654" &&
                request.pago.fechaExpiracion == "06/26" &&
                request.pago.cvv == "456" &&
                request.pago.direccion == "Av. Central 456"
            }
        )
    }

    @Test
    fun `getVentas returns success when remote data source succeeds`() = runTest {
        val ventasList = listOf(mockVentaDto)
        whenever(remoteDataSource.getVentas()).thenReturn(Resource.Success(ventasList))

        val result = repository.getVentas()

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        assertEquals(1, result.data?.first()?.ventaId)
        assertEquals(1500.0, result.data?.first()?.total ?: 0.0, 0.01)
        verify(remoteDataSource).getVentas()
    }

    @Test
    fun `getVentas returns empty list when data is null`() = runTest {
        whenever(remoteDataSource.getVentas()).thenReturn(Resource.Success(null))

        val result = repository.getVentas()

        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data?.isEmpty() == true)
    }

    @Test
    fun `getVentas returns error when remote data source fails`() = runTest {
        whenever(remoteDataSource.getVentas()).thenReturn(Resource.Error("Network error"))

        val result = repository.getVentas()

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
    }

    @Test
    fun `getVentas returns loading when remote data source is loading`() = runTest {
        whenever(remoteDataSource.getVentas()).thenReturn(Resource.Loading())

        val result = repository.getVentas()

        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `getVentas handles multiple ventas correctly`() = runTest {
        val ventasList = listOf(
            mockVentaDto,
            mockVentaDto.copy(ventaId = 2, total = 2500.0),
            mockVentaDto.copy(ventaId = 3, total = 3500.0)
        )
        whenever(remoteDataSource.getVentas()).thenReturn(Resource.Success(ventasList))

        val result = repository.getVentas()

        assertTrue(result is Resource.Success)
        assertEquals(3, (result as Resource.Success).data?.size)
        assertEquals(1500.0, result.data?.get(0)?.total ?: 0.0, 0.01)
        assertEquals(2500.0, result.data?.get(1)?.total ?: 0.0, 0.01)
        assertEquals(3500.0, result.data?.get(2)?.total ?: 0.0, 0.01)
    }

    @Test
    fun `getVenta returns success when remote data source succeeds`() = runTest {
        val ventaId = 1
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Success(mockVentaDto))

        val result = repository.getVenta(ventaId)

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.ventaId)
        assertEquals(1500.0, result.data?.total ?: 0.0, 0.01)
        assertEquals("user123", result.data?.applicationUserId)
        verify(remoteDataSource).getVenta(ventaId)
    }

    @Test
    fun `getVenta returns error when data is null`() = runTest {
        val ventaId = 1
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Success(null))

        val result = repository.getVenta(ventaId)

        assertTrue(result is Resource.Error)
        assertEquals("Venta no encontrada", (result as Resource.Error).message)
    }

    @Test
    fun `getVenta returns error when remote data source fails`() = runTest {
        val ventaId = 1
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Error("Sale not found"))

        val result = repository.getVenta(ventaId)

        assertTrue(result is Resource.Error)
        assertEquals("Sale not found", (result as Resource.Error).message)
    }

    @Test
    fun `getVenta returns loading when remote data source is loading`() = runTest {
        val ventaId = 1
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Loading())

        val result = repository.getVenta(ventaId)

        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `getVenta calls remote data source with correct id`() = runTest {
        val ventaId = 789
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Success(mockVentaDto))

        repository.getVenta(ventaId)

        verify(remoteDataSource).getVenta(eq(789))
    }

    @Test
    fun `getVenta maps all venta properties correctly`() = runTest {
        val ventaId = 1
        whenever(remoteDataSource.getVenta(ventaId)).thenReturn(Resource.Success(mockVentaDto))

        val result = repository.getVenta(ventaId)

        val venta = (result as Resource.Success).data
        assertEquals(1, venta?.ventaId)
        assertEquals("user123", venta?.applicationUserId)
        assertEquals(1500.0, venta?.total ?: 0.0, 0.01)
        assertEquals("2025-12-06T10:00:00", venta?.fecha)
    }
}

