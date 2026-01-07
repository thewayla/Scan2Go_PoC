package sevan.scan2go.poc.data.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Order(
    val id: String = "",
    val totalAmount: Double = 0.0,
    val status: String = "",
    val date: Timestamp? = null,
    val itemCount: Int = 0,
    val items: List<CartItem> = emptyList()
) {
    fun getFormattedDate(): String {
        if (date == null) return ""
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(date.toDate())
    }
}