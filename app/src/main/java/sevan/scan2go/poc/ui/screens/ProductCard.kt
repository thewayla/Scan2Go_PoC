package sevan.scan2go.poc.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import sevan.scan2go.poc.data.model.Product
import sevan.scan2go.poc.ui.theme.SevanGreen
import sevan.scan2go.poc.ui.theme.TextGray

@Composable
fun SevanProductCard(product: Product, onAdd: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl.ifEmpty { "https://sevan.se/storage/DDB497B48AE9A4766FB303A01A4BF40AB39829989FADCC18D6193A2E6BE3E370/df28169f71064e4fba2160d49d4ac650/220-105-0-png.Png/media/2d80dc1385bd4f5dbb1c491765efa82b/sevan501x239.png" },
                    contentDescription = product.name,
                    modifier = Modifier.height(110.dp), // Slightly larger now that badge is gone
                    contentScale = ContentScale.Fit
                )
            }

            Text(text = product.brand, fontSize = 12.sp, color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp,
                minLines = 2,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Artikelnummer ${product.id}", fontSize = 10.sp, color = TextGray)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format("%.2f SEK /kolli", product.packagePrice),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = String.format("Styckpris %.2f SEK", product.pricePerConsumerQuantity),
                fontSize = 11.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color(0xFFE0E0E0))
                    .height(35.dp)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text(
                        "-",
                        color = TextGray
                    )
                }
                Text("1", fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text(
                        "+",
                        color = TextGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("Köp", fontWeight = FontWeight.Bold)
            }
        }
    }
}