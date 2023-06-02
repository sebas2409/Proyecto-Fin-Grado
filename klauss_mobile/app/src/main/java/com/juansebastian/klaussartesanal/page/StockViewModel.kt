package com.juansebastian.klaussartesanal.page

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

    init {
        viewModelScope.launch {
            stockService.getAllStock().let {
                _stock.value = it
            }
        }
    }
}