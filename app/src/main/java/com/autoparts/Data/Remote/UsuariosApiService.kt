package com.autoparts.Data.Remote

import com.autoparts.Data.Remote.Dto.CreateUserDto
import com.autoparts.Data.Remote.Dto.LoginDto
import com.autoparts.Data.Remote.Dto.LoginResponse
import com.autoparts.Data.Remote.Dto.UpdateUserDto
import com.autoparts.Data.Remote.Dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuariosApiService {
    @GET("api/Users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("api/Users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<UserDto>

    @POST("api/Users/register")
    suspend fun register(@Body createUserDto: CreateUserDto): Response<UserDto>

    @POST("login")
    suspend fun login(@Body loginDto: LoginDto): Response<LoginResponse>

    @PUT("api/Users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body updateUserDto: UpdateUserDto): Response<Unit>

    @DELETE("api/Users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>

    @GET("api/Users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<UserDto>

    @GET("api/Users/count")
    suspend fun getUsersCount(): Response<Map<String, Any>>

    @GET("api/Users/test")
    suspend fun test(): Response<Map<String, Any>>
}