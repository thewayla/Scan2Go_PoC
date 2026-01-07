package sevan.scan2go.poc.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.data.repository.CartRepository

class CartViewModel : ViewModel() {
    private val repository = CartRepository()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    val subtotal: StateFlow<Double> get() = MutableStateFlow(_cartItems.value.sumOf { it.totalPrice })

    // 12% Food VAT
    val vatAmount: StateFlow<Double> get() = MutableStateFlow(subtotal.value * 0.12)
    val grandTotal: StateFlow<Double> get() = MutableStateFlow(subtotal.value + vatAmount.value)

    init {
        viewModelScope.launch {
            repository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    fun increment(item: CartItem) {
        viewModelScope.launch { repository.updateQuantity(item.id, item.quantity + 1) }
    }

    fun decrement(item: CartItem) {
        viewModelScope.launch { repository.updateQuantity(item.id, item.quantity - 1) }
    }

    fun remove(item: CartItem) {
        viewModelScope.launch { repository.updateQuantity(item.id, 0) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart(_cartItems.value) }
    }

    fun checkout() {
        repository.placeOrder(_cartItems.value, grandTotal.value)
    }
}