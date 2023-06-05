package com.juansebastian.klaussartesanal.page.stock

import com.juansebastian.klaussartesanal.Stock
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StockService {

    @GET("/api/v1/stock/all")
    suspend fun getAllStock(): List<Stock>

    @POST("/api/v1/stock/update")
    suspend fun updateStock(@Body stock: StockDto): Response<Void>
}