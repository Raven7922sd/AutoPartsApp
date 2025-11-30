package com.autoparts.Data.Repository

import com.autoparts.Data.Mappers.toDomain
import com.autoparts.Data.Mappers.toDto
import com.autoparts.Data.Remote.Resource
import com.autoparts.Data.Remote.api.VentasApiService
import com.autoparts.dominio.model.CreateVenta
import com.autoparts.dominio.model.Venta
import com.autoparts.dominio.repository.VentasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VentasRepositoryImpl @Inject constructor(
    private val ventasApiService: VentasApiService
) : VentasRepository {

    override suspend fun processCheckout(createVenta: CreateVenta): Resource<Venta> =
        withContext(Dispatchers.IO) {
            try {
                val response = ventasApiService.processCheckout(createVenta.toDto())
                Resource.Success(response.toDomain())
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    400 -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        errorBody ?: "El carrito está vacío o hay un error con los datos"
                    }
                    401 -> "No estás autenticado. Por favor inicia sesión."
                    404 -> "Servicio de ventas no disponible"
                    else -> e.message() ?: "Error al procesar la compra"
                }
                Resource.Error(errorMessage)
            } catch (e: IOException) {
                Resource.Error("Error de conexión. Verifica tu internet.")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error desconocido al procesar la compra")
            }
        }

    override suspend fun getVentas(): Resource<List<Venta>> =
        withContext(Dispatchers.IO) {
            try {
                val response = ventasApiService.getVentas()
                Resource.Success(response.map { it.toDomain() })
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "No estás autenticado. Por favor inicia sesión."
                    404 -> "No tienes ventas registradas"
                    else -> e.message() ?: "Error al cargar el historial"
                }
                Resource.Error(errorMessage)
            } catch (e: IOException) {
                Resource.Error("Error de conexión. Verifica tu internet.")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error desconocido al cargar ventas")
            }
        }

    override suspend fun getVenta(ventaId: Int): Resource<Venta> =
        withContext(Dispatchers.IO) {
            try {
                val response = ventasApiService.getVenta(ventaId)
                Resource.Success(response.toDomain())
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "No estás autenticado. Por favor inicia sesión."
                    404 -> "Venta no encontrada"
                    else -> e.message() ?: "Error al cargar la venta"
                }
                Resource.Error(errorMessage)
            } catch (e: IOException) {
                Resource.Error("Error de conexión. Verifica tu internet.")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error desconocido al cargar la venta")
            }
        }
}