package sevan.scan2go.poc.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sevan.scan2go.poc.data.model.CartItem
import sevan.scan2go.poc.data.model.Product
import sevan.scan2go.poc.data.repository.CartRepository
import sevan.scan2go.poc.data.repository.ProductRepository

class HomeViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _scannedProduct = MutableStateFlow<Product?>(null)
    val scannedProduct: StateFlow<Product?> = _scannedProduct

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _scanError = MutableStateFlow<String?>(null)
    val scanError: StateFlow<String?> = _scanError

    val cartCount: StateFlow<Int> get() = MutableStateFlow(_cartItems.value.sumOf { it.quantity })

    private val _externalProduct =
        MutableStateFlow<sevan.scan2go.poc.data.network.OffProduct?>(null)
    val externalProduct: StateFlow<sevan.scan2go.poc.data.network.OffProduct?> = _externalProduct

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    init {
        loadProducts()
        observeCart()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _products.value = productRepository.getAllProducts()
        }
    }

    // REAL-TIME SYNC:
    // We observe the Firestore collection directly. This ensures that if the
    // user updates the cart on a different device (or if we add a web dashboard later),
    // the badge count updates instantly without a manual refresh.
    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    fun onBarcodeScanned(barcode: String) {
        viewModelScope.launch {
            // STRATEGY: Hybrid Data Fetching
            // 1. First, check our internal proprietary catalog (Firestore).
            // This ensures we prioritize our own stock and pricing.
            val localProduct = productRepository.getProductByBarcode(barcode)

            if (localProduct != null) {
                _scannedProduct.value = localProduct
            } else {
                // 2. Fallback: Query External API (OpenFoodFacts) via Retrofit.
                // If the product exists globally but not in our system, we prompt
                // the user to request a stock addition.
                val external = productRepository.fetchExternalProduct(barcode)

                if (external != null) {
                    _externalProduct.value = external
                } else {
                    _scanError.value = barcode
                }
            }
        }
    }

    fun clearScanError() {
        _scanError.value = null
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                cartRepository.addToCart(product)
            } catch (e: Exception) {
                println("Error adding to cart: ${e.message}")
            }
        }
    }

    fun increment(item: CartItem) {
        viewModelScope.launch { cartRepository.updateQuantity(item.id, item.quantity + 1) }
    }

    fun decrement(item: CartItem) {
        viewModelScope.launch { cartRepository.updateQuantity(item.id, item.quantity - 1) }
    }

    fun clearScannedProduct() {
        _scannedProduct.value = null
    }

    fun clearExternalProduct() {
        _externalProduct.value = null
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}