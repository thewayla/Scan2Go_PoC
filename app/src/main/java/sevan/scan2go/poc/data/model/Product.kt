package sevan.scan2go.poc.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val packagePrice: Double = 0.00,
    val quantityPerPackage: Int = 0,
    val pricePerConsumerQuantity: Double = 0.00,
    val imageUrl: String = "",
    val barcode: String = "",
    val brand: String = ""
)