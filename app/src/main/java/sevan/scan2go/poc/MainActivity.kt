package sevan.scan2go.poc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import sevan.scan2go.poc.ui.navigation.Screen
import sevan.scan2go.poc.ui.screens.CartScreen
import sevan.scan2go.poc.ui.screens.HomeScreen
import sevan.scan2go.poc.ui.screens.LoginScreen
import sevan.scan2go.poc.ui.screens.ProfileScreen
import sevan.scan2go.poc.ui.theme.SevanScanToGoPoCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // to seed the database
        //sevan.scan2go.poc.data.repository.ProductRepository().seedDatabase()

        enableEdgeToEdge()
        setContent {
            SevanScanToGoPoCTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) { LoginScreen(navController) }
                        composable(Screen.Home.route) { HomeScreen(navController) }
                        composable(Screen.Cart.route) { CartScreen(navController) }
                        composable(Screen.Profile.route) { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}