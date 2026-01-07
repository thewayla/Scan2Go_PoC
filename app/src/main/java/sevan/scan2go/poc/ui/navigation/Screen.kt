package sevan.scan2go.poc.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Cart : Screen("cart")
    data object Profile : Screen("profile")
}