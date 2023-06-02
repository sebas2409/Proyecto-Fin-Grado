package com.juansebastian.klaussartesanal.page.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.juansebastian.klaussartesanal.ComprobarEstado
import com.juansebastian.klaussartesanal.R
import com.juansebastian.klaussartesanal.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val orders = viewModel.orders.collectAsState(listOf()).value.filter { it.estado != "ENTREGADO" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { Text(text = "Principal") },
                    selected = false,
                    onClick = { navController.navigate(Routes.Home.route) },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.home),
                            contentDescription = "Home",
                            modifier = Modifier.height(32.dp)
                        )
                    })
                NavigationDrawerItem(
                    label = { Text(text = "Stock") },
                    selected = false,
                    onClick = { navController.navigate(Routes.Stock.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.stock),
                            contentDescription = "Stock",
                            modifier = Modifier
                                .height(32.dp)
                        )
                    })
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Pedidos de hoy",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    LazyVerticalGrid(
                        contentPadding = innerPadding,
                        columns = GridCells.Fixed(2),
                    ) {
                        items(count = orders.size) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                onClick = {
                                    navController.navigate("detail/${orders[it].id}/${orders[it].estado}")
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = orders[it].cliente,
                                        modifier = Modifier.padding(8.dp),
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(
                                        text = orders[it].fecha,
                                        modifier = Modifier.padding(8.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = orders[it].estado,
                                        modifier = Modifier.padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = ComprobarEstado.colorear(orders[it].estado)
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}
