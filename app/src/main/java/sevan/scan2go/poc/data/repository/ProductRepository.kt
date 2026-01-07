package sevan.scan2go.poc.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import sevan.scan2go.poc.data.model.Product
import sevan.scan2go.poc.data.network.NetworkClient
import sevan.scan2go.poc.data.network.OffProduct

class ProductRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("products")

    suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        return try {
            val snapshot = collection
                .whereEqualTo("barcode", barcode)
                .limit(1)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /*fun seedDatabase() {
        val products = listOf(
            Product(
                id = "170038",
                name = "Bulgur grov och mörk 1kgx8",
                quantityPerPackage = 8,
                barcode = "7331217012528",
                brand = "Sevan",
                packagePrice = 130.51,
                pricePerConsumerQuantity = 16.31,
                imageUrl = "https://sevan.se/storage/CF69428A51F733BC4BE4378C76881F51BCBEF89C45FB4011E7FDDEFAA66EAAEC/2dbcc45c775b4115983499400f85ad81/500-500-0-jpg.Jpeg/media/8860374f5a1645e4972f85a7ec5c1bc8/170037.jpeg"
            ),
            Product(
                id = "194103",
                name = "Hummus Soltorkad Tomat 275gx6",
                quantityPerPackage = 6,
                barcode = "7331217013013",
                brand = "Sevan",
                packagePrice = 163.33,
                pricePerConsumerQuantity = 27.22,
                imageUrl = "https://sevan.se/storage/F3EFB2825D17FF3A68A9035EECB8934FD099F62F9A32845D46F1806B28DC9847/b4e4390123c14778836d094058cf2336/500-500-0-jpg.Jpeg/media/7af314939a234419b5036c95e9560ff7/194103.jpeg"
            ),
            Product(
                id = "100014",
                name = "Röda Linser Football 900gx15",
                quantityPerPackage = 12,
                barcode = "7331217010166",
                brand = "Sevan",
                packagePrice = 281.88,
                pricePerConsumerQuantity = 18.79,
                imageUrl = "https://sevan.se/storage/12D5ADF4E3E3ABA4C9EEED25DAE312828E2A2C2637F1FC2C8F80C09823450CCF/4f44baf0a8eb46a680b4ec6271a4b0d4/500-500-0-jpg.Jpeg/media/cc8e40dbb2ec41e59bb9a241291b4179/100014.jpeg"
            ),
            Product(
                id = "194197",
                name = "Vit böna kokt fryst Från Sverige 2kgx3",
                quantityPerPackage = 3,
                barcode = "7331217016625",
                brand = "Sevan",
                packagePrice = 250.00,
                pricePerConsumerQuantity = 83.33,
                imageUrl = "https://sevan.se/storage/0342D8A0832928304DFA27C7D69E34513A1501660FC7FBB89CC28C792C96F5CF/96f50c2262ee4a859cdaaf36a367d52c/500-500-0-jpg.Jpeg/media/2797931dd71f4ff7bb346889cee15a3b/194197.jpeg"
            ),
            Product(
                id = "170035",
                name = "Bulgur rostade nudlar 1kgx8",
                quantityPerPackage = 8,
                barcode = "7331217010340",
                brand = "Sevan",
                packagePrice = 133.78,
                pricePerConsumerQuantity = 16.72,
                imageUrl = "https://sevan.se/storage/D4FA13B593143C3FF84714BC6E16DEE92310CBADDD6D548B11B8263A8A5B0A4B/5279636cf0db453688cf8e177e971ce6/500-500-0-jpg.Jpeg/media/2350570b563647f8b6a70470dff644a5/170035.jpeg"
            ),
            Product(
                id = "194124",
                name = "Hummus Original 400gx6",
                quantityPerPackage = 6,
                barcode = "7331217013310",
                brand = "Sevan",
                packagePrice = 169.23,
                pricePerConsumerQuantity = 28.21,
                imageUrl = "https://sevan.se/storage/C6AABA4989DA7BE1F370D3D04F71173BD216F81E8F9455D4FDDF6501D1B4507A/5ee1d50885834c0fac078392572ec1c5/500-500-0-jpg.Jpeg/media/2877de710cc54ecb94b682efb5874070/194124.jpeg"
            ),
            Product(
                id = "520011",
                name = "Vitost Baladi 60% 1500/800gx6",
                quantityPerPackage = 6,
                barcode = "7331217012078",
                brand = "Sevan",
                packagePrice = 491.42,
                pricePerConsumerQuantity = 81.90,
                imageUrl = "https://sevan.se/storage/FDE5512705373F076A5412B73141EF08931C0E71E7304E7BA6492F1B4C5A96B6/3634d45891ea4eb7935e3655fd1a446c/500-500-0-jpg.Jpeg/media/6220811ead7d49538ee3fb89e60334c4/520011.jpeg"
            ),
            Product(
                id = "170039",
                name = "Vete helkorn 1kgx8",
                quantityPerPackage = 8,
                barcode = "7331217010098",
                brand = "Sevan",
                packagePrice = 112.04,
                pricePerConsumerQuantity = 14.01,
                imageUrl = "https://sevan.se/storage/7FBB8AAAA9E714E80AA622627746FE593998B978A3F006CEA878FB6BB0AF4452/0009a5f7af99476c95526e52e2095b77/500-500-0-jpg.Jpeg/media/af604f6bc36a4965995ddc4f0ebe2ea4/170008.jpeg"
            ),
            Product(
                id = "150016",
                name = "Aprikosmarmelad 790gx6",
                quantityPerPackage = 6,
                barcode = "7331217016236",
                brand = "Sevan",
                packagePrice = 210.61,
                pricePerConsumerQuantity = 35.10,
                imageUrl = "https://sevan.se/storage/C3F1D1107619137BBCDF8B1FE137E71F955AC24D80BEAA71E7CF1B37427C7444/174d111aa2b74f94ab4caf23f15de213/500-500-0-jpg.Jpeg/media/ddc3ef2504db44c4ae26764a98f35e4e/150016.jpeg"
            ),
            Product(
                id = "160038",
                name = "Baba Ghannouge 380gx12",
                quantityPerPackage = 12,
                barcode = "7331217014027",
                brand = "Sevan",
                packagePrice = 202.44,
                pricePerConsumerQuantity = 16.87,
                imageUrl = "https://sevan.se/storage/4E477580915766A221080CD8237CFC95748FC553BDF1291744F1CD3FC1011C04/baae871e92d04e5ba2f8ee137f59345c/500-500-0-jpg.Jpeg/media/1d220d6aa3684fb99baffbebca2cdd25/160038.jpeg"
            ),
            Product(
            id = "194101",
            name = "Hummus Original 275gx6",
            quantityPerPackage = 6,
            barcode = "7331217012993",
            brand = "Sevan",
            packagePrice = 133.33,
            pricePerConsumerQuantity = 22.22,
            imageUrl = "https://sevan.se/storage/5A0B49C06DB492571B6F85F61E9168A8D431651CE492695723AFCA89B13E8920/a11fd25792164d5f8c01e821af1b22b5/500-500-0-jpg.Jpeg/media/3bdf873c5fda487999e6bc0bc8132593/194101.jpeg"
        )
        )

        products.forEach { product ->
            collection.document(product.id).set(product)
        }
    }*/

    suspend fun fetchExternalProduct(barcode: String): OffProduct? {
        return try {
            val response = NetworkClient.api.getProduct(barcode)
            if (response.status == 1) {
                response.product
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}