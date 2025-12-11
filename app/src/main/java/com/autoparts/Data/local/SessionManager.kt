package com.autoparts.Data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveSession(userId: String, email: String, userName: String?, jwtToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
            userName?.let { preferences[USER_NAME_KEY] = it }
            preferences[JWT_TOKEN_KEY] = jwtToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun getUserId(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[USER_ID_KEY]
    }

    val userIdFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[JWT_TOKEN_KEY] != null
    }

    suspend fun getUserEmail(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[USER_EMAIL_KEY]
    }

    suspend fun getUserName(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[USER_NAME_KEY]
    }

    suspend fun getJwtToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[JWT_TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[REFRESH_TOKEN_KEY]
    }

    suspend fun isLoggedIn(): Boolean {
        return getJwtToken() != null
    }

    suspend fun getSessionData(): SessionData? {
        val preferences = context.dataStore.data.first()
        val token = preferences[JWT_TOKEN_KEY]

        return if (token != null) {
            SessionData(
                userId = preferences[USER_ID_KEY] ?: "",
                email = preferences[USER_EMAIL_KEY] ?: "",
                userName = preferences[USER_NAME_KEY],
                jwtToken = token,
                refreshToken = preferences[REFRESH_TOKEN_KEY] ?: ""
            )
        } else {
            null
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

data class SessionData(
    val userId: String,
    val email: String,
    val userName: String?,
    val jwtToken: String,
    val refreshToken: String
)