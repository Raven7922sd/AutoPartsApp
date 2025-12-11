package com.autoparts.Data.local

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistentCookieJar @Inject constructor(
    private val context: Context
) : CookieJar {

    private val TAG = "PersistentCookieJar"
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()
    private val cookieFile = File(context.filesDir, "cookies.json")
    private val moshi = Moshi.Builder().build()

    init {
        loadCookies()
        Log.d(TAG, "Initialized. Cookies loaded: ${cookieStore.size} hosts")
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        Log.d(TAG, "Saving cookies for $host: ${cookies.size} cookies")

        if (cookieStore[host] == null) {
            cookieStore[host] = mutableListOf()
        }

        cookies.forEach { cookie ->
            Log.d(TAG, "  Cookie: ${cookie.name} = ${cookie.value.take(20)}... (expires: ${cookie.expiresAt})")
        }

        cookieStore[host]?.removeAll { cookie ->
            cookies.any { it.name == cookie.name }
        }

        cookieStore[host]?.addAll(cookies.filter { !it.expiresAt.isBefore(System.currentTimeMillis()) })

        persistCookies()
        Log.d(TAG, "Cookies saved. Total in store: ${cookieStore[host]?.size ?: 0}")
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        val now = System.currentTimeMillis()

        cookieStore[host]?.removeAll { it.expiresAt < now }

        val cookies = cookieStore[host]?.filter { !it.expiresAt.isBefore(now) } ?: emptyList()

        Log.d(TAG, "Loading cookies for $host: ${cookies.size} cookies")
        cookies.forEach { cookie ->
            Log.d(TAG, "  Sending Cookie: ${cookie.name} = ${cookie.value.take(20)}...")
        }

        return cookies
    }

    fun clearAllCookies() {
        Log.d(TAG, "Clearing all cookies")
        cookieStore.clear()
        persistCookies()
    }

    private fun persistCookies() {
        try {
            val serializable = cookieStore.mapValues { entry ->
                entry.value.map { cookie ->
                    SerializableCookie(
                        name = cookie.name,
                        value = cookie.value,
                        expiresAt = cookie.expiresAt,
                        domain = cookie.domain,
                        path = cookie.path,
                        secure = cookie.secure,
                        httpOnly = cookie.httpOnly,
                        hostOnly = cookie.hostOnly
                    )
                }
            }

            val type = Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                Types.newParameterizedType(List::class.java, SerializableCookie::class.java)
            )
            val adapter = moshi.adapter<Map<String, List<SerializableCookie>>>(type)
            cookieFile.writeText(adapter.toJson(serializable))
            Log.d(TAG, "Cookies persisted to file: ${cookieFile.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Error persisting cookies", e)
        }
    }

    private fun loadCookies() {
        try {
            if (!cookieFile.exists()) {
                Log.d(TAG, "No cookies file found")
                return
            }

            val type = Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                Types.newParameterizedType(List::class.java, SerializableCookie::class.java)
            )
            val adapter = moshi.adapter<Map<String, List<SerializableCookie>>>(type)
            val serializable = adapter.fromJson(cookieFile.readText()) ?: return

            serializable.forEach { (host, cookies) ->
                cookieStore[host] = cookies.map { it.toCookie() }.toMutableList()
                Log.d(TAG, "Loaded ${cookies.size} cookies for $host")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading cookies", e)
        }
    }

    private data class SerializableCookie(
        val name: String,
        val value: String,
        val expiresAt: Long,
        val domain: String,
        val path: String,
        val secure: Boolean,
        val httpOnly: Boolean,
        val hostOnly: Boolean
    ) {
        fun toCookie(): Cookie {
            return Cookie.Builder()
                .name(name)
                .value(value)
                .expiresAt(expiresAt)
                .apply {
                    if (hostOnly) {
                        hostOnlyDomain(domain)
                    } else {
                        domain(domain)
                    }
                }
                .path(path)
                .apply {
                    if (secure) secure()
                    if (httpOnly) httpOnly()
                }
                .build()
        }
    }

    private fun Long.isBefore(other: Long): Boolean = this < other
}