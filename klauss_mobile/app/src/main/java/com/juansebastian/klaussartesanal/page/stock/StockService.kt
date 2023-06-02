package com.juansebastian.klaussartesanal.page.stock

import com.juansebastian.klaussartesanal.Stock
import retrofit2.http.GET

interface StockService {

    @GET("/api/v1/stock/all")
    suspend fun getAllStock(): List<Stock>
}