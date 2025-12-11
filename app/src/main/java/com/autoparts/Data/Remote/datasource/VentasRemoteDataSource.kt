package com.autoparts.data.remote.datasource

import com.autoparts.data.remote.api.VentasApiService
import com.autoparts.data.remote.dto.venta.VentaDto
import com.autoparts.data.remote.dto.venta.CheckoutRequest
import com.autoparts.data.remote.dto.ventas.EstadisticasVentasDto
import com.autoparts.data.remote.dto.ventas.VentasPaginadasResponseDto
import com.autoparts.data.remote.util.Resource
import javax.inject.Inject

class VentasRemoteDataSource @Inject constructor(
    private val apiService: VentasApiService
) {
    companion object {
        private const val ERROR_DESCONOCIDO = "Error desconocido"
    }

    suspend fun processCheckout(checkoutRequest: CheckoutRequest): Resource<VentaDto> {
        return try {
            val response = apiService.processCheckout(checkoutRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Error al procesar compra: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun getVentas(): Resource<List<VentaDto>> {
        return try {
            val response = apiService.getVentas()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun getVenta(ventaId: Int): Resource<VentaDto> {
        return try {
            val response = apiService.getVenta(ventaId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(message = "Venta no encontrada")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: ERROR_DESCONOCIDO)
        }
    }

    suspend fun getAllVentas(
        pagina: Int = 1,
        tamanoPagina: Int = 10,
        fechaDesde: String? = null,
        fechaHasta: String? = null,
        usuarioId: String? = null
    ): Resource<VentasPaginadasResponseDto> {
        return try {
            val response = apiService.getAllVentas(
                pagina = pagina,
                tamanoPagina = tamanoPagina,
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta,
                usuarioId = usuarioId
            )

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(
                    message = "Error al obtener ventas: ${response.code()}"
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "$ERROR_DESCONOCIDO al obtener ventas"
            )
        }
    }

    suspend fun getEstadisticas(
        fechaDesde: String? = null,
        fechaHasta: String? = null
    ): Resource<EstadisticasVentasDto> {
        return try {
            val response = apiService.getEstadisticas(
                fechaDesde = fechaDesde,
                fechaHasta = fechaHasta
            )

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(
                    message = "Error al obtener estadísticas: ${response.code()}"
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "$ERROR_DESCONOCIDO al obtener estadísticas"
            )
        }
    }
}