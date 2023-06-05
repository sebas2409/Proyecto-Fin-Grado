package com.juansebastian.klaussartesanal.page.stock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.juansebastian.klaussartesanal.R
import com.juansebastian.klaussartesanal.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockPage(
    navController: NavHostController,
    viewModel: StockViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val stock = viewModel.stock.collectAsState().value
    val value = viewModel.value.collectAsState().value
    val openDialog = remember { mutableStateOf(false) }
    val (cantidad, setCantidad) = remember { mutableStateOf(1000) }
    val (id, setId) = remember { mutableStateOf("") }
    val context = LocalContext.current

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
                                "Stock de productos",
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        item {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextField(value = value.toString(), onValueChange = {
                                    if (it.isNotEmpty()) {
                                        viewModel.changeValue(it.toInt())
                                    } else {
                                        viewModel.changeValue(0)
                                    }
                                }, placeholder = {
                                    Text(text = "Establecer mínimo de stock")
                                }, label = {
                                    Text(text = "Mínimo de stock")
                                })
                            }
                        }
                        items(stock.size) { product ->
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = stock[product].nombre)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = stock[product].cantidad.toString(),
                                    color = if (stock[product].cantidad < value) Color(0xFFFF5722) else Color(
                                        0xFF4CAF50
                                    )
                                )
                                if (stock[product].cantidad < value) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    TextButton(onClick = {
                                        openDialog.value = true
                                        setId(stock[product].id)
                                    }) {
                                        Text(text = "Comprar")
                                    }
                                }
                            }
                        }
                    }
                    if (openDialog.value) {
                        AlertDialog(onDismissRequest = { openDialog.value = false },
                            title = { Text(text = "Actualizar Stock") },
                            text = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    TextField(value = cantidad.toString(), onValueChange = {
                                        if (it.isNotEmpty() && it.isDigitsOnly()) {
                                            setCantidad(it.toInt())
                                        } else {
                                            setCantidad(1000)
                                        }
                                    }, placeholder = {
                                        Text(text = "1000")
                                    }, label = {
                                        Text(text = "Cantidad a comprar")
                                    })
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.updateStock(
                                        id,
                                        cantidad,
                                        context
                                    )
                                    setCantidad(1000)
                                    openDialog.value = false
                                    navController.navigate(Routes.Home.route)
                                }) {
                                    Text(text = "Aceptar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { openDialog.value = false }) {
                                    Text(text = "Cancelar")
                                }
                            }
                        )

                    }
                })
        }
    )
}