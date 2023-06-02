package com.juansebastian.klaussartesanal.page

import com.juansebastian.klaussartesanal.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderService {

    @GET("api/v1/order/all")
    suspend fun getAllOrders(): ArrayList<Order>

    @POST("api/v1/order/state")
    suspend fun updateOrder(@Body OrderDto: OrderDto)
}