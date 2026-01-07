package sevan.scan2go.poc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import sevan.scan2go.poc.data.model.Order
import sevan.scan2go.poc.ui.theme.BackgroundGray
import sevan.scan2go.poc.ui.theme.SevanGreen
import sevan.scan2go.poc.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()

) {
    val orders by viewModel.orders.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            Surface(color = SevanGreen, shadowElevation = 4.dp, modifier = Modifier.height(60.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = rememberAsyncImagePainter("https://sevan.se/storage/DDB497B48AE9A4766FB303A01A4BF40AB39829989FADCC18D6193A2E6BE3E370/df28169f71064e4fba2160d49d4ac650/220-105-0-png.Png/media/2d80dc1385bd4f5dbb1c491765efa82b/sevan501x239.png"),
                        contentDescription = "Logo",
                        modifier = Modifier.height(36.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "MIN SIDOR",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SevanGreen
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BackgroundGray,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            null,
                            tint = TextGray,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(viewModel.userEmail, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Kundnummer: 900234", fontSize = 12.sp, color = TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Mina Ordrar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (orders.isEmpty()) {
                Text("Inga ordrar funna.", color = TextGray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(orders) { order ->
                        OrderRow(order, viewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                    val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                        com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                    ).build()
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso)
                        .signOut()
                    navController.navigate(sevan.scan2go.poc.ui.navigation.Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Logga Ut")
            }
        }
    }
}

@Composable
fun OrderRow(order: Order, viewModel: ProfileViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Order #${order.id.takeLast(6).uppercase()}", fontWeight = FontWeight.Bold)
                    Text(order.getFormattedDate(), fontSize = 12.sp, color = TextGray)
                    Text("${order.itemCount} varor", fontSize = 12.sp, color = TextGray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        String.format("%.2f SEK", order.totalAmount),
                        fontWeight = FontWeight.Bold,
                        color = SevanGreen
                    )
                    Surface(
                        color = if (order.status == "BEHANDLAS") Color(0xFFE8F5E9) else Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            order.status,
                            fontSize = 10.sp,
                            color = if (order.status == "BEHANDLAS") SevanGreen else Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (expanded) {
                Divider(color = BackgroundGray)
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Produkter",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.imageUrl.ifEmpty { "https://via.placeholder.com/50" }),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    "${item.quantity} st x ${item.price} kr",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                            Text(
                                String.format("%.2f kr", item.totalPrice),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.reorder(order)
                            android.widget.Toast.makeText(
                                context,
                                "Produkterna har lagts i varukorgen",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text("Beställ igen")
                    }
                }
            }
        }
    }
}