package com.juansebastian.klaussartesanal

data class Order(
    val id: String,
    val products: List<OrderProduct>,
    val total: Int,
    val cliente: String,
    val estado: String,
    val fecha: String,
)
