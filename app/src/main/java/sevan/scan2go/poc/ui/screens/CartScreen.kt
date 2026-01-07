package sevan.scan2go.poc.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.ui.theme.BackgroundGray
import sevan.scan2go.poc.ui.theme.SevanGreen
import sevan.scan2go.poc.ui.theme.SevanRed
import sevan.scan2go.poc.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.totalPrice }
    val vat = subtotal * 0.12
    val total = subtotal + vat
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Surface(
                color = SevanGreen,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = rememberAsyncImagePainter("https://sevan.se/storage/DDB497B48AE9A4766FB303A01A4BF40AB39829989FADCC18D6193A2E6BE3E370/df28169f71064e4fba2160d49d4ac650/220-105-0-png.Png/media/2d80dc1385bd4f5dbb1c491765efa82b/sevan501x239.png"),
                        contentDescription = "Logo",
                        modifier = Modifier.height(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(modifier = Modifier.size(48.dp))
                }
            }
        },
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Din varukorg är tom", color = TextGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BackgroundGray),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "KASSA",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SevanGreen
                    )
                    Text("Varukorg", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                items(cartItems) { item ->
                    CartItemRow(item, viewModel)
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            "Töm hela varukorgen",
                            textDecoration = TextDecoration.Underline,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { viewModel.clearCart() }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SummaryRow("Kvar till fri frakt", "Fri frakt", isBold = false)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Summering", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))

                        SummaryRow("Ordinarie pris", String.format("%.2f SEK", subtotal))
                        SummaryRow("Delsumma", String.format("%.2f SEK", subtotal))
                        SummaryRow("Fraktkostnad", "0,00 SEK")

                        Spacer(modifier = Modifier.height(8.dp))
                        SummaryRow("Totalsumma", String.format("%.2f SEK", subtotal), isBold = true)
                        SummaryRow("Moms", String.format("%.2f SEK", vat))

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Totalsumma (ink. moms)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                String.format("%.2f SEK", total),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 24.dp))

                        CheckoutForm()

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.checkout()
                                Toast.makeText(context, "Order mottagen!", Toast.LENGTH_LONG).show()
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Bekräfta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutForm() {
    // Form State
    var firstName by remember { mutableStateOf("Olle") }
    var lastName by remember { mutableStateOf("Marmenlind") }
    var phone by remember { mutableStateOf("0724263799") }
    var email by remember { mutableStateOf("olle@sevan.se") }
    var acceptedTerms by remember { mutableStateOf(false) }

    // --- DATE LOGIC ---
    var showDatePicker by remember { mutableStateOf(false) }

    // 1. Calculate Minimum Date (Today + 2 Days)
    val calendar = java.util.Calendar.getInstance()
    calendar.add(java.util.Calendar.DAY_OF_YEAR, 2)
    // Reset time to midnight to ensure exact comparison
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    val minValidDate = calendar.timeInMillis

    // 2. Create Validator
    val dateValidator = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= minValidDate
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year >= calendar.get(java.util.Calendar.YEAR)
        }
    }

    // 3. Setup Picker with Validator
    val datePickerState = rememberDatePickerState(selectableDates = dateValidator)
    val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

    // 4. Default Display: If no date selected, show the calculated min date
    val displayDate = datePickerState.selectedDateMillis?.let {
        dateFormatter.format(java.util.Date(it))
    } ?: dateFormatter.format(calendar.time)
    // ------------------

    Text("Referens", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.weight(1f),
            label = { Text("Förnamn") },
            shape = RectangleShape
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.weight(1f),
            label = { Text("Efternamn") },
            shape = RectangleShape
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Telefon") },
        shape = RectangleShape
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("E-post") },
        shape = RectangleShape
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Addresses
    Text("Fakturaadress", fontWeight = FontWeight.Bold)
    Text("TESTKUND Grundkund 0%\n19572 ROSERSBERG\nSweden", color = TextGray, fontSize = 14.sp)

    Spacer(modifier = Modifier.height(16.dp))

    Text("Leveransadress", fontWeight = FontWeight.Bold)
    Text(
        "TESTKUND Grundkund 0%\nMETALLVÄGEN 59\n19572 ROSERSBERG\nSweden",
        color = TextGray,
        fontSize = 14.sp
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Delivery
    Text("Leveranssätt.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        RadioButton(
            selected = true,
            onClick = {},
            colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
        )
        Text("Standardfrakt")
    }

    // DATE PICKER FIELD
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = displayDate,
            onValueChange = {},
            label = { Text("Önskat leveransdatum (Minst 2 dagar framåt)") },
            trailingIcon = { Icon(Icons.Default.DateRange, null) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = RectangleShape
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = SevanGreen)
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = SevanRed)
                ) {
                    Text("Avbryt")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                // Apply the validator here to gray out invalid dates
                showModeToggle = false
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Payment
    Text("Betalsätt", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = true,
            onClick = {},
            colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
        )
        Text("Faktura")
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Terms
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = acceptedTerms,
            onCheckedChange = { acceptedTerms = it },
            colors = CheckboxDefaults.colors(checkedColor = SevanGreen)
        )
        Text(
            "Jag har läst och accepterar villkoren",
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun CartItemRow(item: CartItem, viewModel: CartViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl.ifEmpty { "https://via.placeholder.com/100" }),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SevanGreen)
                Text(item.id, fontSize = 12.sp, color = TextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(String.format("%.2f SEK", item.totalPrice), fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                ) {
                    IconButton(
                        onClick = { viewModel.decrement(item) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            "-",
                            tint = TextGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(
                        onClick = { viewModel.increment(item) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "+",
                            tint = TextGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { viewModel.remove(item) }) {
                    Icon(Icons.Default.Delete, "Remove", tint = SevanRed)
                }
            }
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = if (isBold) Color.Black else TextGray,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            color = if (isBold) Color.Black else TextGray,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}