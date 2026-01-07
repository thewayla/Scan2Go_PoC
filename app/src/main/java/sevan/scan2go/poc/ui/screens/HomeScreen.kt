package sevan.scan2go.poc.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.ui.theme.BackgroundGray
import sevan.scan2go.poc.ui.theme.SevanGreen
import sevan.scan2go.poc.ui.theme.SevanRed
import sevan.scan2go.poc.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState()
    val scannedProduct by viewModel.scannedProduct.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val cartCount = cartItems.sumOf { it.quantity }

    var showCartPreview by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    val externalProduct by viewModel.externalProduct.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val scanError by viewModel.scanError.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BackgroundGray,
            topBar = {
                Surface(
                    color = SevanGreen,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp)
                    ) {

                        IconButton(
                            onClick = { },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Image(
                            painter = rememberAsyncImagePainter("https://sevan.se/storage/DDB497B48AE9A4766FB303A01A4BF40AB39829989FADCC18D6193A2E6BE3E370/df28169f71064e4fba2160d49d4ac650/220-105-0-png.Png/media/2d80dc1385bd4f5dbb1c491765efa82b/sevan501x239.png"),
                            contentDescription = "Sevan Logo",
                            modifier = Modifier
                                .height(40.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Fit
                        )

                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.navigate(sevan.scan2go.poc.ui.navigation.Screen.Profile.route) }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Log Out",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        showCartPreview = !showCartPreview
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    "Cart",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                                if (cartCount > 0) {
                                    Surface(
                                        color = SevanRed,
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 2.dp, end = 2.dp)
                                            .size(20.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                cartCount.toString(),
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.offset(y = (-1).dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scanner.startScan().addOnSuccessListener { barcode ->
                            if (barcode.rawValue != null) viewModel.onBarcodeScanned(barcode.rawValue!!)
                        }
                    },
                    containerColor = SevanGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, "Scan", modifier = Modifier.size(38.dp))
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "PRODUKTER",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SevanGreen,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Visar ${products.size} Produkter", fontSize = 12.sp, color = TextGray)
                }

                if (products.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = SevanGreen)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products) { product ->
                            SevanProductCard(product = product, onAdd = {
                                viewModel.addToCart(product)
                                Toast.makeText(
                                    context,
                                    "Lade till ${product.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }
                    }
                }
            }
        }

        if (showCartPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showCartPreview = false }
                    .background(Color.Transparent)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 8.dp)
            ) {
                CartPreviewPopup(
                    items = cartItems,
                    onCheckout = {
                        showCartPreview = false
                        navController.navigate(sevan.scan2go.poc.ui.navigation.Screen.Cart.route)
                    },
                    onIncrement = { viewModel.increment(it) },
                    onDecrement = { viewModel.decrement(it) }
                )
            }
        }
    }

    if (scannedProduct != null) {
        var quantity by remember { mutableStateOf(1) }

        AlertDialog(
            containerColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { viewModel.clearScannedProduct() },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Artikel hittades",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = SevanGreen,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = scannedProduct!!.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${scannedProduct!!.packagePrice} SEK", color = TextGray)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            .height(48.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Remove, "-", tint = TextGray)
                        }
                        Text(
                            text = quantity.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(onClick = { quantity++ }, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.Default.Add, "+", tint = TextGray)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        for (i in 1..quantity) viewModel.addToCart(scannedProduct!!)
                        viewModel.clearScannedProduct()
                        Toast.makeText(
                            context,
                            "$quantity x ${scannedProduct!!.name} tillagd",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lägg till i varukorg")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.clearScannedProduct() },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Avbryt")
                }
            }
        )
    }

    if (externalProduct != null) {
        AlertDialog(
            containerColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { viewModel.clearExternalProduct() },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Produkten saknas i sortimentet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Vi hittade följande produkt i en extern databas:",
                        fontSize = 14.sp,
                        color = TextGray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${externalProduct?.brands ?: "Okänt märke"} - ${externalProduct?.name ?: "Okänd produkt"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = SevanGreen
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Vill du skicka en förfrågan om att vi ska ta in denna produkt?",
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = TextGray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Tack! Din förfrågan har skickats.",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.clearExternalProduct()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ja, skicka förfrågan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.clearExternalProduct() },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Nej, avbryt")
                }
            }
        )
    }

    if (scanError != null) {
        AlertDialog(
            containerColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { viewModel.clearScanError() },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = SevanRed,
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Ingen träff",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Vi hittade ingen produkt med koden:",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = TextGray
                    )
                    Text(
                        scanError!!,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearScanError()
                        scanner.startScan().addOnSuccessListener { barcode ->
                            if (barcode.rawValue != null) viewModel.onBarcodeScanned(barcode.rawValue!!)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Försök igen")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.clearScanError() },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Avbryt")
                }
            }
        )
    }
}

@Composable
fun CartPreviewPopup(
    items: List<CartItem>,
    onCheckout: () -> Unit,
    onIncrement: (CartItem) -> Unit,
    onDecrement: (CartItem) -> Unit
) {
    val total = items.sumOf { it.totalPrice }

    Surface(
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(0.dp),
        color = Color.White,
        modifier = Modifier.width(320.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0))
                    .padding(12.dp)
            ) {
                Text(
                    "Varukorg",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (items.isEmpty()) {
                Text(
                    "Varukorgen är tom", modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally), color = TextGray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items) { item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(item.imageUrl.ifEmpty { "https://via.placeholder.com/50" }),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 8.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(String.format("%.2f SEK", item.totalPrice), fontSize = 12.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.border(
                                    1.dp,
                                    Color.LightGray,
                                    RoundedCornerShape(4.dp)
                                )
                            ) {
                                IconButton(
                                    onClick = { onDecrement(item) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Remove,
                                        "-",
                                        tint = TextGray,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                Text(
                                    item.quantity.toString(),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                IconButton(
                                    onClick = { onIncrement(item) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        "+",
                                        tint = TextGray,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Divider()

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Totalt", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Kvar till fri frakt", fontSize = 10.sp, color = TextGray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            String.format("%.2f SEK", total),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text("Fri frakt", fontSize = 10.sp, color = TextGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCheckout,
                    colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Text("KASSA", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}