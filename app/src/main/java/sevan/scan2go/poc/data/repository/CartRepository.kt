package sevan.scan2go.poc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.data.model.Product

class CartRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    private val cartCollection
        get() = db.collection("users").document(userId).collection("cart")

    fun addToCart(product: Product) {
        val docRef = cartCollection.document(product.id)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            if (snapshot.exists()) {
                val currentQty = snapshot.getLong("quantity") ?: 0
                transaction.update(docRef, "quantity", currentQty + 1)
            } else {
                val newItem = CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.packagePrice,
                    quantity = 1,
                    imageUrl = product.imageUrl
                )
                transaction.set(docRef, newItem)
            }
        }
    }

    fun updateQuantity(itemId: String, newQty: Int) {
        if (newQty <= 0) {
            cartCollection.document(itemId).delete()
        } else {
            cartCollection.document(itemId).update("quantity", newQty)
        }
    }

    fun getCartItems(): Flow<List<CartItem>> = callbackFlow {
        val listener: ListenerRegistration = cartCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.toObjects(CartItem::class.java)
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    fun clearCart(items: List<CartItem>) {
        val batch = db.batch()
        items.forEach { item ->
            val ref = cartCollection.document(item.id)
            batch.delete(ref)
        }
        batch.commit()
    }

    fun placeOrder(items: List<CartItem>, totalAmount: Double) {
        val batch = db.batch()

        // ARCHITECTURE: Atomic Batch Write
        // We must ensure that creating the order and clearing the cart happen
        // simultaneously. If one fails, both must fail to prevent data inconsistency.

        val orderRef = db.collection("orders").document()
        // ... set order data ...

        val orderData = hashMapOf(
            "userId" to userId,
            "totalAmount" to totalAmount,
            "status" to "BEHANDLAS",
            "date" to com.google.firebase.Timestamp.now(),
            "itemCount" to items.sumOf { it.quantity },
            "items" to items
        )
        batch.set(orderRef, orderData)

        items.forEach { item ->
            // Delete item from cart sub-collection
            val ref = cartCollection.document(item.id)
            batch.delete(ref)
        }

        batch.commit()
    }

    fun addItemsToCart(items: List<CartItem>) {
        val batch = db.batch()

        items.forEach { item ->
            val docRef = cartCollection.document(item.id)
            // THIS IS A SHORTCUT: For a real app, use a Cloud Function.
            // For this PoC, we will simply loop the existing addToCart logic.
        }
    }
}