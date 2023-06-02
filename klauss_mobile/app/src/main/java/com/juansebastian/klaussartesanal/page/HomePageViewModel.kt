package com.juansebastian.klaussartesanal.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juansebastian.klaussartesanal.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(private val orderService: OrderService) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(listOf())
    val orders = _orders.asStateFlow()

    init {
        viewModelScope.launch {
            orderService.getAllOrders()
                .let { _orders.value = it;Log.d("HomePageViewModel", "orders: $it") }

        }
    }
}