package com.autoparts.di

import android.content.Context
import com.autoparts.Data.Remote.AuthInterceptor
import com.autoparts.Data.Remote.CarritoApiService
import com.autoparts.Data.Remote.ProductosApiService
import com.autoparts.Data.Remote.UsuariosApiService
import com.autoparts.Data.Remote.api.VentasApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://autopartsap1-production.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideJugadorApiService(retrofit: Retrofit): UsuariosApiService {
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
}