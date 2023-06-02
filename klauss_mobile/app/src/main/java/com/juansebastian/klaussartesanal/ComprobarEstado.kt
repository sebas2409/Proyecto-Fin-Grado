package com.juansebastian.klaussartesanal

import androidx.compose.ui.graphics.Color

object ComprobarEstado {
    fun colorear(estado: String): Color {
        return when (estado) {
            "RECIBIDO" -> Color(0xFFFF5722)
            "EN COCINA" -> Color(0xFF4CAF50)
            "REPARTO" -> Color(0xFF2196F3)
            else -> Color(0xFF0D0F0D)
        }
    }
}