package com.autoparts.Data.Remote

import com.autoparts.Data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        val publicEndpoints = listOf(
            "/login",
            "/register",
            "/api/Productos",
            "/api/Users/test",
            "/api/Users/count"
        )

        if (publicEndpoints.any { url.contains(it) }) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            try {
                sessionManager.getJwtToken()
            } catch (e: Exception) {
                null
            }
        }

        if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(newRequest)
        }

        return chain.proceed(originalRequest)
    }
}