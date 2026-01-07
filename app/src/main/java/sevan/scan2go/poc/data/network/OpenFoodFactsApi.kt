package sevan.scan2go.poc.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class OffResponse(
    val status: Int, // 1 = found, 0 = not found
    val product: OffProduct?
)

data class OffProduct(
    @SerializedName("product_name") val name: String?,
    val brands: String?
)

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): OffResponse
}

object NetworkClient {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}