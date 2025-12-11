package com.autoparts.data.repository

import com.autoparts.data.remote.datasource.VentasRemoteDataSource
import com.autoparts.data.remote.dto.venta.CheckoutRequest
import com.autoparts.data.remote.dto.venta.PagoRequest
import com.autoparts.data.remote.mapper.EstadisticasMapper
import com.autoparts.data.remote.mapper.VentaMapper
import com.autoparts.data.remote.mapper.VentasAdminMapper
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateVenta
import com.autoparts.domain.model.EstadisticasVentas
import com.autoparts.domain.model.Venta
import com.autoparts.domain.model.VentasPaginadas
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class VentasRepositoryImpl @Inject constructor(
    private val remoteDataSource: VentasRemoteDataSource
) : VentasRepository {

    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
    }

    override suspend fun processCheckout(createVenta: CreateVenta): Resource<Venta> {
        val request = CheckoutRequest(
            pago = PagoRequest(
                nombreTitular = createVenta.nombreTitular,
                numeroTarjeta = createVenta.numeroTarjeta,
                fechaExpiracion = createVenta.fechaExpiracion,
                cvv = createVenta.cvv,
                direccion = createVenta.direccion
            )
        )

        return when (val result = remoteDataSource.processCheckout(request)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(VentaMapper.toDomain(dto))
                } ?: Resource.Error("Error al procesar compra")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getVentas(): Resource<List<Venta>> {
        return when (val result = remoteDataSource.getVentas()) {
            is Resource.Success -> {
                val ventas = result.data?.map { VentaMapper.toDomain(it) } ?: emptyList()
                Resource.Success(ventas)
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getVenta(ventaId: Int): Resource<Venta> {
        return when (val result = remoteDataSource.getVenta(ventaId)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(VentaMapper.toDomain(dto))
                } ?: Resource.Error("Venta no encontrada")
            }
            is Resource.Error -> Resource.Error(result.message ?: ERROR_DESCONOCIDO)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getAllVentas(
        pagina: Int,
        tamanoPagina: Int,
        fechaDesde: String?,
        fechaHasta: String?,
        usuarioId: String?
    ): Resource<VentasPaginadas> {
        return when (val result = remoteDataSource.getAllVentas(
            pagina, tamanoPagina, fechaDesde, fechaHasta, usuarioId
        )) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(VentasAdminMapper.toDomain(dto))
                } ?: Resource.Error("Error al obtener ventas")
            }
            is Resource.Error -> Resource.Error(
                result.message ?: ERROR_DESCONOCIDO
            )
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getEstadisticas(
        fechaDesde: String?,
        fechaHasta: String?
    ): Resource<EstadisticasVentas> {
        return when (val result = remoteDataSource.getEstadisticas(
            fechaDesde, fechaHasta
        )) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    Resource.Success(EstadisticasMapper.toDomain(dto))
                } ?: Resource.Error("Error al obtener estadísticas")
            }
            is Resource.Error -> Resource.Error(
                result.message ?: ERROR_DESCONOCIDO
            )
            is Resource.Loading -> Resource.Loading()
        }
    }
}

