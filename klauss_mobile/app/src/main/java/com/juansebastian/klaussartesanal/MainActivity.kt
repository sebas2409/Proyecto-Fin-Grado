package com.juansebastian.klaussartesanal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juansebastian.klaussartesanal.page.detail.DetailPage
import com.juansebastian.klaussartesanal.page.home.HomePage
import com.juansebastian.klaussartesanal.page.stock.StockPage
import com.juansebastian.klaussartesanal.ui.theme.KlaussArtesanalTheme
import com.pusher.pushnotifications.PushNotifications
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlaussArtesanalTheme {
                PushNotifications.start(applicationContext, "7de718c8-4fbe-4413-b02d-4bca1430dcc8")
                PushNotifications.addDeviceInterest("hello")
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home.route) {
                    composable(Routes.Home.route) {
                        HomePage(navController)
                    }
                    composable(Routes.Detail.route + "/{id}/{estado}", arguments = listOf(
                        navArgument("id") { type = NavType.StringType },
                        navArgument("estado") { type = NavType.StringType }
                    )) {
                        it.arguments?.getString("id")?.let { id ->
                            it.arguments?.getString("estado")?.let { estado ->
                                DetailPage(navController, id, estado)
                            }
                        }
                    }
                    composable(Routes.Stock.route) {
                        StockPage(navController)
                    }
                }
            }
        }
    }
}


