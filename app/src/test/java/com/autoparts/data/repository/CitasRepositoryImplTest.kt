package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.CitasRemoteDataSource
import com.autoparts.data.remote.dto.cita.CitaDto
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateCita
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class CitasRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: CitasRemoteDataSource

    private lateinit var repository: CitasRepositoryImpl

    private val mockCitaDto = CitaDto(
        citaId = 1,
        applicationUserId = "user123",
        clienteNombre = "Juan Pérez",
        servicioSolicitado = "Cambio de aceite",
        fechaCita = "2025-12-10T10:00:00",
        confirmada = false,
        codigoConfirmacion = "ABC123"
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CitasRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getCitas returns success when remote data source succeeds`() = runTest {
        val citasList = listOf(mockCitaDto)
        whenever(remoteDataSource.getCitas()).thenReturn(Resource.Success(citasList))

        val result = repository.getCitas()

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        assertEquals(1, result.data?.first()?.citaId)
        assertEquals("Juan Pérez", result.data?.first()?.clienteNombre)
        verify(remoteDataSource).getCitas()
    }

    @Test
    fun `getCitas returns empty list when data is null`() = runTest {
        whenever(remoteDataSource.getCitas()).thenReturn(Resource.Success(null))

        val result = repository.getCitas()

        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data?.isEmpty() == true)
        verify(remoteDataSource).getCitas()
    }

    @Test
    fun `getCitas returns error when remote data source fails`() = runTest {
        whenever(remoteDataSource.getCitas()).thenReturn(Resource.Error("Network error"))

        val result = repository.getCitas()

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
        verify(remoteDataSource).getCitas()
    }

    @Test
    fun `getCitas returns loading when remote data source is loading`() = runTest {
        whenever(remoteDataSource.getCitas()).thenReturn(Resource.Loading())

        val result = repository.getCitas()

        assertTrue(result is Resource.Loading)
        verify(remoteDataSource).getCitas()
    }

    @Test
    fun `createCita returns success when remote data source succeeds`() = runTest {
        val createCita = CreateCita(
            clienteNombre = "Juan Pérez",
            servicioSolicitado = "Cambio de aceite",
            fechaCita = "2025-12-10T10:00:00"
        )
        whenever(remoteDataSource.createCita(any())).thenReturn(Resource.Success(mockCitaDto))

        val result = repository.createCita(createCita)

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.citaId)
        assertEquals("Juan Pérez", result.data?.clienteNombre)
        assertEquals("Cambio de aceite", result.data?.servicioSolicitado)
        verify(remoteDataSource).createCita(any())
    }

    @Test
    fun `createCita returns error when data is null`() = runTest {
        val createCita = CreateCita(
            clienteNombre = "Juan Pérez",
            servicioSolicitado = "Cambio de aceite",
            fechaCita = "2025-12-10T10:00:00"
        )
        whenever(remoteDataSource.createCita(any())).thenReturn(Resource.Success(null))

        val result = repository.createCita(createCita)

        assertTrue(result is Resource.Error)
        assertEquals("Error al crear cita", (result as Resource.Error).message)
        verify(remoteDataSource).createCita(any())
    }

    @Test
    fun `createCita returns error when remote data source fails`() = runTest {
        val createCita = CreateCita(
            clienteNombre = "Juan Pérez",
            servicioSolicitado = "Cambio de aceite",
            fechaCita = "2025-12-10T10:00:00"
        )
        whenever(remoteDataSource.createCita(any())).thenReturn(Resource.Error("Server error"))

        val result = repository.createCita(createCita)

        assertTrue(result is Resource.Error)
        assertEquals("Server error", (result as Resource.Error).message)
        verify(remoteDataSource).createCita(any())
    }

    @Test
    fun `createCita returns loading when remote data source is loading`() = runTest {
        val createCita = CreateCita(
            clienteNombre = "Juan Pérez",
            servicioSolicitado = "Cambio de aceite",
            fechaCita = "2025-12-10T10:00:00"
        )
        whenever(remoteDataSource.createCita(any())).thenReturn(Resource.Loading())

        val result = repository.createCita(createCita)

        assertTrue(result is Resource.Loading)
        verify(remoteDataSource).createCita(any())
    }

    @Test
    fun `cancelCita returns success when remote data source succeeds`() = runTest {
        val citaId = 1
        whenever(remoteDataSource.cancelCita(citaId)).thenReturn(Resource.Success(Unit))

        val result = repository.cancelCita(citaId)

        assertTrue(result is Resource.Success)
        verify(remoteDataSource).cancelCita(citaId)
    }

    @Test
    fun `cancelCita returns error when remote data source fails`() = runTest {
        val citaId = 1
        whenever(remoteDataSource.cancelCita(citaId)).thenReturn(Resource.Error("Cannot cancel"))

        val result = repository.cancelCita(citaId)

        assertTrue(result is Resource.Error)
        assertEquals("Cannot cancel", (result as Resource.Error).message)
        verify(remoteDataSource).cancelCita(citaId)
    }

    @Test
    fun `cancelCita calls remote data source with correct id`() = runTest {
        val citaId = 123
        whenever(remoteDataSource.cancelCita(citaId)).thenReturn(Resource.Success(Unit))

        repository.cancelCita(citaId)

        verify(remoteDataSource).cancelCita(eq(123))
    }

    @Test
    fun `createCita maps domain model to request correctly`() = runTest {
        val createCita = CreateCita(
            clienteNombre = "María González",
            servicioSolicitado = "Reparación de frenos",
            fechaCita = "2025-12-15T14:30:00"
        )
        whenever(remoteDataSource.createCita(any())).thenReturn(Resource.Success(mockCitaDto))

        repository.createCita(createCita)

        verify(remoteDataSource).createCita(
            argThat { request ->
                request.clienteNombre == "María González" &&
                request.servicioSolicitado == "Reparación de frenos" &&
                request.fechaCita == "2025-12-15T14:30:00"
            }
        )
    }
}