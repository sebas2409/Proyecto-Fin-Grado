package com.juansebastian.klaussartesanal.page.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun DetailPage(
    navController: NavHostController,
    id: String,
    estado: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    viewModel.getOrderById(id)
    val order = viewModel.order.collectAsState().value

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
                                "Detalle del pedido",
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        order?.id?.let { Text(text = "Id: $it") }
                        Spacer(modifier = Modifier.height(8.dp))
                        order?.cliente?.let { Text(text = "Telefono: $it") }
                        Spacer(modifier = Modifier.height(8.dp))
                        order?.fecha?.let { Text(text = it) }
                        Spacer(modifier = Modifier.height(8.dp))
                        order?.products?.forEach { op ->
                            Text(text = op.nombre, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Cantidad: ${op.cantidad}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = estado,
                            fontWeight = FontWeight.Bold,
                            color = ComprobarEstado.colorear(estado)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            viewModel.updateOrder(id, estado) {
                                navController.navigate(Routes.Home.route)
                            }
                        }) {
                            Text(text = "Actualizar estado")
                        }
                    }
                }
            )
        }
    )
}