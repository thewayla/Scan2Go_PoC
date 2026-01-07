package sevan.scan2go.poc.data.model

data class CartItem(
    val id: String = "",        // Product SKU
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = ""
) {

    val totalPrice: Double
        get() = price * quantity
}