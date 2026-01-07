package sevan.scan2go.poc.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sevan.scan2go.poc.data.model.Order
import sevan.scan2go.poc.data.model.Product
import sevan.scan2go.poc.data.repository.CartRepository
import sevan.scan2go.poc.data.repository.OrderRepository

class ProfileViewModel : ViewModel() {
    private val repository = OrderRepository()
    private val cartRepository = CartRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    val userEmail: String = auth.currentUser?.email ?: "Gästkonto"
    val userId: String = auth.currentUser?.uid ?: "Unknown"

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _orders.value = repository.getUserOrders()
        }
    }

    fun reorder(order: Order) {
        viewModelScope.launch {
            order.items.forEach { item ->
                val product = Product(
                    id = item.id,
                    name = item.name,
                    packagePrice = item.price,
                    imageUrl = item.imageUrl
                )

                for (i in 1..item.quantity) {
                    cartRepository.addToCart(product)
                }
            }
        }
    }
}