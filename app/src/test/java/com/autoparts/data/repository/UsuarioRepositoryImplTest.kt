package com.autoparts.data.repository

import com.autoparts.data.local.manager.SessionManager
import com.autoparts.data.remote.datasource.UsuarioRemoteDataSource
import com.autoparts.data.remote.dto.usuario.LoginResult
import com.autoparts.data.remote.dto.usuario.UsuarioDto
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateUser
import com.autoparts.domain.model.UpdateUser
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class UsuarioRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: UsuarioRemoteDataSource

    @Mock
    private lateinit var sessionManager: SessionManager

    private lateinit var repository: UsuarioRepositoryImpl

    private val mockUsuarioDto = UsuarioDto(
        id = "user123",
        userName = "Johan Pérez",
        email = "johan@example.com",
        phoneNumber = "829-123-4567",
        emailConfirmed = true,
        roles = listOf("User")
    )

    private val mockLoginResult = LoginResult(
        accessToken = "mock-jwt-token",
        expiresIn = 3600,
        refreshToken = "mock-refresh-token",
        usuario = mockUsuarioDto
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = UsuarioRepositoryImpl(remoteDataSource, sessionManager)
    }

    @Test
    fun `register returns success when remote data source succeeds`() = runTest {
        val createUser = CreateUser(
            email = "nuevo@example.com",
            password = "Password123!",
            phoneNumber = "829-999-8888"
        )
        whenever(remoteDataSource.register(any())).thenReturn(Resource.Success(mockUsuarioDto))
        val result = repository.register(createUser)
        assertTrue(result is Resource.Success)
        assertEquals("user123", (result as Resource.Success).data?.id)
        assertEquals("johan@example.com", result.data?.email)
        verify(remoteDataSource).register(any())
    }

    @Test
    fun `register returns error when data is null`() = runTest {
        val createUser = CreateUser(
            email = "nuevo@example.com",
            password = "Password123!",
            phoneNumber = null
        )
        whenever(remoteDataSource.register(any())).thenReturn(Resource.Success(null))
        val result = repository.register(createUser)
        assertTrue(result is Resource.Error)
        assertEquals("Error al registrar usuario", (result as Resource.Error).message)
    }

    @Test
    fun `register returns error when remote data source fails`() = runTest {
        val createUser = CreateUser(
            email = "nuevo@example.com",
            password = "Password123!",
            phoneNumber = null
        )
        whenever(remoteDataSource.register(any())).thenReturn(Resource.Error("Email already exists"))
        val result = repository.register(createUser)
        assertTrue(result is Resource.Error)
        assertEquals("Email already exists", (result as Resource.Error).message)
    }

    @Test
    fun `register maps domain model to request correctly`() = runTest {
        val createUser = CreateUser(
            email = "test@example.com",
            password = "SecurePass123!",
            phoneNumber = "123-456-7890"
        )
        whenever(remoteDataSource.register(any())).thenReturn(Resource.Success(mockUsuarioDto))
        repository.register(createUser)
        verify(remoteDataSource).register(
            argThat { request ->
                request.email == "test@example.com" &&
                request.password == "SecurePass123!" &&
                request.phoneNumber == "123-456-7890"
            }
        )
    }

    @Test
    fun `login returns success and saves session data`() = runTest {
        val email = "johan@example.com"
        val password = "Password123!"
        whenever(remoteDataSource.login(any())).thenReturn(Resource.Success(mockLoginResult))
        val result = repository.login(email, password)
        assertTrue(result is Resource.Success)
        assertEquals("user123", (result as Resource.Success).data?.id)
        verify(sessionManager).saveUserEmail(mockUsuarioDto.email)
        verify(sessionManager).saveUserId("user123")
        verify(sessionManager).saveToken("mock-jwt-token")
        verify(remoteDataSource).login(any())
    }

    @Test
    fun `login uses email as userId when id is null`() = runTest {
        val email = "test@example.com"
        val password = "Password123!"
        val dtoWithNullId = mockUsuarioDto.copy(id = null)
        val loginResultWithNullId = mockLoginResult.copy(usuario = dtoWithNullId)
        whenever(remoteDataSource.login(any())).thenReturn(Resource.Success(loginResultWithNullId))
        repository.login(email, password)
        verify(sessionManager).saveUserId(dtoWithNullId.email)
    }

    @Test
    fun `login returns error when data is null`() = runTest {
        val email = "test@example.com"
        val password = "Password123!"
        whenever(remoteDataSource.login(any())).thenReturn(Resource.Success(null))
        val result = repository.login(email, password)
        assertTrue(result is Resource.Error)
        assertEquals("Error al iniciar sesión", (result as Resource.Error).message)
    }

    @Test
    fun `login returns error when remote data source fails`() = runTest {
        val email = "test@example.com"
        val password = "WrongPassword"
        whenever(remoteDataSource.login(any())).thenReturn(Resource.Error("Invalid credentials"))
        val result = repository.login(email, password)
        assertTrue(result is Resource.Error)
        assertEquals("Invalid credentials", (result as Resource.Error).message)
    }

    @Test
    fun `logout clears session`() = runTest {
        repository.logout()
        verify(sessionManager).clearSession()
    }

    @Test
    fun `updateUsuario returns success when remote data source succeeds`() = runTest {
        val userId = "user123"
        val updateUser = UpdateUser(
            userName = "New Name",
            email = "newemail@example.com",
            phoneNumber = "999-888-7777",
            currentPassword = "OldPass123!",
            newPassword = "NewPass123!"
        )
        whenever(remoteDataSource.updateUsuario(eq(userId), any())).thenReturn(Resource.Success(Unit))
        val result = repository.updateUsuario(userId, updateUser)
        assertTrue(result is Resource.Success)
        verify(remoteDataSource).updateUsuario(eq(userId), any())
    }

    @Test
    fun `updateUsuario maps domain model to request correctly`() = runTest {
        val userId = "user123"
        val updateUser = UpdateUser(
            userName = null,
            email = "updated@example.com",
            phoneNumber = "111-222-3333",
            currentPassword = null,
            newPassword = null
        )
        whenever(remoteDataSource.updateUsuario(eq(userId), any())).thenReturn(Resource.Success(Unit))
        repository.updateUsuario(userId, updateUser)
        verify(remoteDataSource).updateUsuario(
            eq(userId),
            argThat { request ->
                request.email == "updated@example.com" &&
                request.phoneNumber == "111-222-3333"
            }
        )
    }

    @Test
    fun `getUsuarioByEmail returns success when remote data source succeeds`() = runTest {
        val email = "johan@example.com"
        whenever(remoteDataSource.getUsuarioByEmail(email)).thenReturn(Resource.Success(mockUsuarioDto))
        val result = repository.getUsuarioByEmail(email)
        assertTrue(result is Resource.Success)
        assertEquals("user123", (result as Resource.Success).data?.id)
        assertEquals(email, result.data?.email)
        verify(remoteDataSource).getUsuarioByEmail(email)
    }

    @Test
    fun `getUsuarioByEmail returns error when data is null`() = runTest {
        val email = "notfound@example.com"
        whenever(remoteDataSource.getUsuarioByEmail(email)).thenReturn(Resource.Success(null))
        val result = repository.getUsuarioByEmail(email)
        assertTrue(result is Resource.Error)
        assertEquals("Usuario no encontrado", (result as Resource.Error).message)
    }

    @Test
    fun `getUsuarioByEmail returns error when remote data source fails`() = runTest {
        val email = "test@example.com"
        whenever(remoteDataSource.getUsuarioByEmail(email)).thenReturn(Resource.Error("User not found"))
        val result = repository.getUsuarioByEmail(email)
        assertTrue(result is Resource.Error)
        assertEquals("User not found", (result as Resource.Error).message)
    }

    @Test
    fun `register returns loading when remote data source is loading`() = runTest {
        val createUser = CreateUser(
            email = "test@example.com",
            password = "Pass123!",
            phoneNumber = null
        )
        whenever(remoteDataSource.register(any())).thenReturn(Resource.Loading())
        val result = repository.register(createUser)
        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `login returns loading when remote data source is loading`() = runTest {
        whenever(remoteDataSource.login(any())).thenReturn(Resource.Loading())
        val result = repository.login("test@example.com", "Pass123!")
        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `getUsuarioByEmail returns loading when remote data source is loading`() = runTest {
        whenever(remoteDataSource.getUsuarioByEmail(any())).thenReturn(Resource.Loading())
        val result = repository.getUsuarioByEmail("test@example.com")
        assertTrue(result is Resource.Loading)
    }
}

