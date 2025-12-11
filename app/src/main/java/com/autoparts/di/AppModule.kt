package com.autoparts.di

import android.content.Context
import androidx.room.Room
import com.autoparts.data.remote.interceptor.AuthInterceptor
import com.autoparts.data.remote.api.CarritoApiService
import com.autoparts.data.remote.api.ProductosApiService
import com.autoparts.data.remote.api.UsuariosApiService
import com.autoparts.data.remote.api.VentasApiService
import com.autoparts.data.remote.api.ServiciosApiService
import com.autoparts.data.remote.api.CitasApiService
import com.autoparts.data.local.database.AutoPartsDatabase
import com.autoparts.data.local.dao.UsuarioDao
import com.autoparts.data.local.manager.CarritoLocalManager
import com.autoparts.data.local.manager.SessionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAutoPartsDatabase(
        @ApplicationContext context: Context
    ): AutoPartsDatabase {
        return Room.databaseBuilder(
            context,
            AutoPartsDatabase::class.java,
            "autoparts_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUsuarioDao(database: AutoPartsDatabase): UsuarioDao {
        return database.usuarioDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://autopartsap1-production.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideUsuariosApiService(retrofit: Retrofit): UsuariosApiService {
        return retrofit.create(UsuariosApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductosApiService(retrofit: Retrofit): ProductosApiService {
        return retrofit.create(ProductosApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCarritoApiService(retrofit: Retrofit): CarritoApiService {
        return retrofit.create(CarritoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideVentasApiService(retrofit: Retrofit): VentasApiService {
        return retrofit.create(VentasApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideServiciosApiService(retrofit: Retrofit): ServiciosApiService {
        return retrofit.create(ServiciosApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCitasApiService(retrofit: Retrofit): CitasApiService {
        return retrofit.create(CitasApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager = SessionManager(context)

    @Provides
    @Singleton
    fun provideCarritoLocalManager(
        @ApplicationContext context: Context
    ): CarritoLocalManager = CarritoLocalManager(context)
}