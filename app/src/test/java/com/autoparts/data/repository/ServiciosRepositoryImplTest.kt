package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.ServiciosRemoteDataSource
import com.autoparts.data.remote.dto.servicio.ServicioDto
import com.autoparts.data.remote.util.Resource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ServiciosRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: ServiciosRemoteDataSource

    private lateinit var repository: ServiciosRepositoryImpl

    private val mockServicioDto = ServicioDto(
        servicioId = 1,
        nombre = "Cambio de Aceite",
        precio = 500.0,
        descripcion = "Cambio de aceite y filtro",
        duracionEstimada = 1.0,
        servicioImagenBase64 = "base64string",
        solicitados = 10,
        fechaServicio = "2025-12-06"
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = ServiciosRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getServicios returns success when remote data source succeeds`() = runTest {
        val serviciosList = listOf(mockServicioDto)
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Success(serviciosList))
        val result = repository.getServicios()
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        assertEquals(1, result.data?.first()?.servicioId)
        assertEquals("Cambio de Aceite", result.data?.first()?.nombre)
        verify(remoteDataSource).getServicios()
    }

    @Test
    fun `getServicios returns empty list when data is null`() = runTest {
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Success(null))
        val result = repository.getServicios()
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data?.isEmpty() == true)
        verify(remoteDataSource).getServicios()
    }

    @Test
    fun `getServicios returns error when remote data source fails`() = runTest {
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Error("Network error"))
        val result = repository.getServicios()
        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
        verify(remoteDataSource).getServicios()
    }

    @Test
    fun `getServicios returns loading when remote data source is loading`() = runTest {
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Loading())
        val result = repository.getServicios()
        assertTrue(result is Resource.Loading)
        verify(remoteDataSource).getServicios()
    }

    @Test
    fun `getServicio returns success when remote data source succeeds`() = runTest {
        val servicioId = 1
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Success(mockServicioDto))
        val result = repository.getServicio(servicioId)
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.servicioId)
        assertEquals("Cambio de Aceite", result.data?.nombre)
        assertEquals(500.0, result.data?.precio ?: 0.0, 0.01)
        verify(remoteDataSource).getServicio(servicioId)
    }

    @Test
    fun `getServicio returns error when data is null`() = runTest {
        val servicioId = 1
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Success(null))
        val result = repository.getServicio(servicioId)
        assertTrue(result is Resource.Error)
        assertEquals("Servicio no encontrado", (result as Resource.Error).message)
        verify(remoteDataSource).getServicio(servicioId)
    }

    @Test
    fun `getServicio returns error when remote data source fails`() = runTest {
        val servicioId = 1
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Error("Service not found"))
        val result = repository.getServicio(servicioId)
        assertTrue(result is Resource.Error)
        assertEquals("Service not found", (result as Resource.Error).message)
        verify(remoteDataSource).getServicio(servicioId)
    }

    @Test
    fun `getServicio returns loading when remote data source is loading`() = runTest {
        val servicioId = 1
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Loading())
        val result = repository.getServicio(servicioId)
        assertTrue(result is Resource.Loading)
        verify(remoteDataSource).getServicio(servicioId)
    }

    @Test
    fun `getServicio calls remote data source with correct id`() = runTest {
        val servicioId = 456
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Success(mockServicioDto))
        repository.getServicio(servicioId)
        verify(remoteDataSource).getServicio(eq(456))
    }

    @Test
    fun `getServicios maps all service properties correctly`() = runTest {
        val serviciosList = listOf(mockServicioDto)
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Success(serviciosList))
        val result = repository.getServicios()
        val servicio = (result as Resource.Success).data?.first()
        assertEquals(1, servicio?.servicioId)
        assertEquals("Cambio de Aceite", servicio?.nombre)
        assertEquals("Cambio de aceite y filtro", servicio?.descripcion)
        assertEquals(500.0, servicio?.precio ?: 0.0, 0.01)
        assertEquals(1.0, servicio?.duracionEstimada ?: 0.0, 0.01)
        assertEquals(10, servicio?.solicitados)
    }

    @Test
    fun `getServicios handles multiple services correctly`() = runTest {
        val serviciosList = listOf(
            mockServicioDto,
            mockServicioDto.copy(servicioId = 2, nombre = "Alineación"),
            mockServicioDto.copy(servicioId = 3, nombre = "Balanceo")
        )
        whenever(remoteDataSource.getServicios()).thenReturn(Resource.Success(serviciosList))
        val result = repository.getServicios()
        assertTrue(result is Resource.Success)
        assertEquals(3, (result as Resource.Success).data?.size)
        assertEquals("Cambio de Aceite", result.data?.get(0)?.nombre)
        assertEquals("Alineación", result.data?.get(1)?.nombre)
        assertEquals("Balanceo", result.data?.get(2)?.nombre)
    }

    @Test
    fun `getServicio with different servicioId returns correct servicio`() = runTest {
        val servicioId = 99
        val differentServicio = mockServicioDto.copy(
            servicioId = 99,
            nombre = "Reparación de Motor"
        )
        whenever(remoteDataSource.getServicio(servicioId)).thenReturn(Resource.Success(differentServicio))
        val result = repository.getServicio(servicioId)
        assertTrue(result is Resource.Success)
        assertEquals(99, (result as Resource.Success).data?.servicioId)
        assertEquals("Reparación de Motor", result.data?.nombre)
    }
}

