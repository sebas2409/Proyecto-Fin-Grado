package com.juansebastian.klaussartesanal.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juansebastian.klaussartesanal.DetailService
import com.juansebastian.klaussartesanal.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailService: DetailService,
    private val orderService: OrderService
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order = _order.asStateFlow()

    fun getOrderById(id: String) {
        viewModelScope.launch {
            detailService.getOrderById(id).let {
                _order.value = it
                Log.d("DetailViewModel", "order: $it")
            }
        }
    }

    fun updateOrder(id: String, estado: String, backToHome: () ->Unit) {
        when (estado) {
            "RECIBIDO" -> {
                viewModelScope.launch {
                    orderService.updateOrder(OrderDto(id, "EN COCINA"))
                    backToHome()
                }
            }

            "EN COCINA" -> {
                viewModelScope.launch {
                    orderService.updateOrder(OrderDto(id, "REPARTO"))
                    backToHome()
                }
            }

            "REPARTO" -> {
                viewModelScope.launch {
                    orderService.updateOrder(OrderDto(id, "ENTREGADO"))
                    backToHome()
                }
            }
        }
    }
}