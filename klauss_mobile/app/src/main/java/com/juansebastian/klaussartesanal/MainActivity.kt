package com.juansebastian.klaussartesanal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juansebastian.klaussartesanal.page.DetailPage
import com.juansebastian.klaussartesanal.page.HomePage
import com.juansebastian.klaussartesanal.page.StockPage
import com.juansebastian.klaussartesanal.ui.theme.KlaussArtesanalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlaussArtesanalTheme {
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


