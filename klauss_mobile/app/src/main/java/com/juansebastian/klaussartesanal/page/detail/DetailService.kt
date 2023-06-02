package com.juansebastian.klaussartesanal.page.detail

import com.juansebastian.klaussartesanal.Order
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {

    @GET("/api/v1/order/{id}")
    suspend fun getOrderById(@Path("id") id: String): Order

}