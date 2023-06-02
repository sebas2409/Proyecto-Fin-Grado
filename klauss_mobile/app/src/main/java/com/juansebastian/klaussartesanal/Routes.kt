package com.juansebastian.klaussartesanal

sealed class Routes(val route:String){
    object Home: Routes("home")
    object Detail: Routes("detail")
    object Stock: Routes("stock")
}
