package com.autoparts.Data.Remote

import com.autoparts.Data.Remote.Dto.ProductoDto
import javax.inject.Inject

class ProductoRemoteDataSource @Inject constructor(
    private val apiService: ProductosApiService
) {
    suspend fun getProductos(): Resource<List<ProductoDto>> {
        return try {
            val response = apiService.getProductos()
            if(response.isSuccessful){
                response.body().let{ Resource.Success(it)}
            }else {
                return Resource.Error("Error ${response.code()}: ${response.message()} ")
            }
        }catch (e: Exception){
            return Resource.Error("Error ${e.localizedMessage} ?: Ocurrió un error al obtener los productos ")
        }
    }

    suspend fun getProducto(id: Int): Resource<ProductoDto?> {
        return try {
            val response = apiService.getProducto(id)
            if(response.isSuccessful){
                response.body().let{ Resource.Success(it)}
            }else{
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            return Resource.Error("Error ${e.localizedMessage} ?: Ocurrio un error al obtener el producto")
        }
    }

    suspend fun putProducto(id: Int, producto: ProductoDto): Resource<Unit> {
        return try{
            val response = apiService.putProducto(id, producto)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            return Resource.Error("Error ${e.localizedMessage} ?: Error al actualizar el producto")
        }
    }

    suspend fun postProducto(producto: ProductoDto): Resource<ProductoDto?> {
        return try{
            val response = apiService.postProducto(producto)
            if(response.isSuccessful){
                response.body().let{ Resource.Success(it)}
            }else{
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            return Resource.Error("Error ${e.localizedMessage}?: Error al crear el producto")
        }
    }
}

