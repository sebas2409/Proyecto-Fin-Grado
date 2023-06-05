package com.juansebastian.klaussartesanal.page.stock

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juansebastian.klaussartesanal.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockService: StockService
) : ViewModel() {

    private val _stock = MutableStateFlow<List<Stock>>(listOf())
    val stock = _stock.asStateFlow()

    private val _value = MutableStateFlow(5000)
    val value = _value.asStateFlow()

    init {
        viewModelScope.launch {
            stockService.getAllStock().let {
                _stock.value = it
            }
        }
    }

    fun changeValue(value: Int) {
        _value.value = value
    }

    fun updateStock(id: String, cantidad: Int, context: Context) {
        viewModelScope.launch {
            val response = stockService.updateStock(StockDto(id, cantidad))
            if (response.isSuccessful) {
                Toast.makeText(context, "Stock actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al actualizar el stock", Toast.LENGTH_SHORT).show()
            }
        }
    }
}