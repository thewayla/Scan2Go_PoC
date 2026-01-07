package sevan.scan2go.poc.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import sevan.scan2go.poc.ui.navigation.Screen
import sevan.scan2go.poc.ui.theme.SevanGreen

@Composable
fun LoginScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val webClientId = "59761577452-9n6v5cvb6ind0p6d3es3i1lp2cefj2si.apps.googleusercontent.com"

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        isLoading = false
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        Toast.makeText(
                            context,
                            "Firebase Auth Failed: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } catch (e: ApiException) {
                isLoading = false
                Toast.makeText(context, "Google Sign In Failed: ${e.statusCode}", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = rememberAsyncImagePainter("https://sevan.se/storage/DDB497B48AE9A4766FB303A01A4BF40AB39829989FADCC18D6193A2E6BE3E370/df28169f71064e4fba2160d49d4ac650/220-105-0-png.Png/media/2d80dc1385bd4f5dbb1c491765efa82b/sevan501x239.png"),
            contentDescription = "Sevan Logo",
            modifier = Modifier.fillMaxWidth(0.5f),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.weight(3f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SevanGreen)
                }
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SevanGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Logga in med Google")
                }

                Button(
                    // STRATEGY: Low-Friction Entry
                    // We allow anonymous "Guest" access to let sales reps test the
                    // scanning flow immediately without credential friction.
                    // Firestore rules are configured to allow these temporary sessions to write to their own cart.
                    onClick = {
                        isLoading = true
                        auth.signInAnonymously().addOnSuccessListener {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Fortsätt som gäst")
                }
            }
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}