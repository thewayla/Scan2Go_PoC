package sevan.scan2go.poc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.data.model.Order

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getUserOrders(): List<Order> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = db.collection("orders")
                .whereEqualTo("userId", uid)
                .get()
                .await()

            snapshot.documents.map { doc ->
                val rawItems = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                val cartItems = rawItems.map { itemMap ->
                    CartItem(
                        id = itemMap["id"] as? String ?: "",
                        name = itemMap["name"] as? String ?: "",
                        price = (itemMap["price"] as? Number)?.toDouble() ?: 0.0,
                        quantity = (itemMap["quantity"] as? Long)?.toInt() ?: 0,
                        imageUrl = itemMap["imageUrl"] as? String ?: ""
                    )
                }

                Order(
                    id = doc.id,
                    totalAmount = doc.getDouble("totalAmount") ?: 0.0,
                    status = doc.getString("status") ?: "Unknown",
                    date = doc.getTimestamp("date"),
                    itemCount = doc.getLong("itemCount")?.toInt() ?: 0,
                    items = cartItems
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}