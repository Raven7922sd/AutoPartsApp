package com.autoparts.di

import com.autoparts.Data.Repository.ProductoRepositoryImpl
import com.autoparts.Data.Repository.UsuarioRepositoryImpl
import com.autoparts.Data.Repository.CarritoRepositoryImpl
import com.autoparts.Data.Repository.VentasRepositoryImpl
import com.autoparts.dominio.repository.ProductoRepository
import com.autoparts.dominio.repository.UsuarioRepository
import com.autoparts.dominio.repository.CarritoRepository
import com.autoparts.dominio.repository.VentasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGastoRepository(
        impl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        impl: ProductoRepositoryImpl
    ): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        impl: CarritoRepositoryImpl
    ): CarritoRepository

    @Binds
    @Singleton
    abstract fun bindVentasRepository(
        impl: VentasRepositoryImpl
    ): VentasRepository
}