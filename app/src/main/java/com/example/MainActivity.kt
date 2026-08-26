package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.model.SpaceCategory
import com.example.data.repository.TerrariumRepository
import com.example.ui.components.TerrariumBottomNav
import com.example.ui.components.TerrariumTopBar
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.cart.CartAndCheckoutScreen
import com.example.ui.screens.catalogue.CatalogueScreen
import com.example.ui.screens.dashboard.CustomerDashboardScreen
import com.example.ui.screens.detail.ProductDetailScreen
import com.example.ui.screens.guides.CareGuidesScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.kits.PlantKitsScreen
import com.example.ui.screens.order.OrderTrackingScreen
import com.example.ui.screens.quiz.PlantMatchQuizScreen
import com.example.ui.screens.reminders.CareRemindersScreen
import com.example.ui.screens.space.ShopBySpaceScreen
import com.example.ui.screens.welcome.WelcomeAuthScreen
import com.example.ui.screens.wishlist.WishlistScreen
import com.example.ui.theme.TerrariumTheme
import com.example.ui.viewmodel.TerrariumViewModel
import com.example.ui.viewmodel.TerrariumViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = TerrariumRepository(database)

        setContent {
            TerrariumTheme {
                val viewModel: TerrariumViewModel = viewModel(
                    factory = TerrariumViewModelFactory(repository)
                )
                TerrariumApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TerrariumApp(viewModel: TerrariumViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "welcome"

    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val totalCartCount = cartItems.sumOf { it.quantity }
    val totalWishlistCount = wishlistItems.size

    // Main top-level navigation routes where bottom bar & top bar should appear
    val isTopLevelCustomerScreen = currentRoute in listOf("home", "catalogue", "quiz", "care", "dashboard")
    val showTopBar = currentRoute !in listOf("welcome", "admin")
    val showBottomBar = currentRoute !in listOf("welcome", "admin")

    Scaffold(
        topBar = {
            if (showTopBar) {
                TerrariumTopBar(
                    cartItemCount = totalCartCount,
                    wishlistItemCount = totalWishlistCount,
                    isAdminMode = isAdminMode,
                    onCartClick = { navController.navigate("cart") },
                    onWishlistClick = { navController.navigate("wishlist") },
                    onToggleAdminClick = { viewModel.toggleAdminMode() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar && !isAdminMode) {
                TerrariumBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "welcome"
            ) {
                // Welcome / Role Selector
                composable("welcome") {
                    WelcomeAuthScreen(
                        onExploreClick = {
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        },
                        onQuizClick = {
                            navController.navigate("quiz") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        },
                        onLoginSuccess = { name, email ->
                            viewModel.loginUser(name, email)
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        }
                    )
                }

                // Home Explore
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCatalogue = {
                            navController.navigate("catalogue")
                        },
                        onNavigateToQuiz = {
                            navController.navigate("quiz")
                        },
                        onNavigateToGuides = {
                            navController.navigate("care")
                        },
                        onNavigateToSpace = { space: SpaceCategory ->
                            navController.navigate("space/${space.name}")
                        },
                        onNavigateToProductDetail = { productId: Long ->
                            navController.navigate("detail/$productId")
                        },
                        onNavigateToBundles = {
                            navController.navigate("kits")
                        }
                    )
                }

                // All Products Catalogue
                composable("catalogue") {
                    CatalogueScreen(
                        viewModel = viewModel,
                        onNavigateToProductDetail = { productId ->
                            navController.navigate("detail/$productId")
                        }
                    )
                }

                // Product Details
                composable(
                    route = "detail/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getLong("productId") ?: 1L
                    ProductDetailScreen(
                        productId = productId,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToCheckout = { navController.navigate("cart") },
                        onNavigateToGuides = { navController.navigate("care") }
                    )
                }

                // Plant Match Quiz
                composable("quiz") {
                    PlantMatchQuizScreen(
                        viewModel = viewModel,
                        onNavigateToProductDetail = { productId ->
                            navController.navigate("detail/$productId")
                        }
                    )
                }

                // Shop By Space
                composable(
                    route = "space/{spaceName}",
                    arguments = listOf(navArgument("spaceName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val spaceName = backStackEntry.arguments?.getString("spaceName") ?: SpaceCategory.BEDROOM.name
                    val spaceCategory = try {
                        SpaceCategory.valueOf(spaceName)
                    } catch (e: Exception) {
                        SpaceCategory.BEDROOM
                    }

                    ShopBySpaceScreen(
                        initialSpace = spaceCategory,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToProductDetail = { productId ->
                            navController.navigate("detail/$productId")
                        }
                    )
                }

                // Complete Plant-Care Kits & Bundles
                composable("kits") {
                    PlantKitsScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Wishlist
                composable("wishlist") {
                    WishlistScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToProductDetail = { productId ->
                            navController.navigate("detail/$productId")
                        },
                        onExploreClick = {
                            navController.navigate("catalogue")
                        }
                    )
                }

                // Cart & Checkout
                composable("cart") {
                    CartAndCheckoutScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onExploreClick = { navController.navigate("catalogue") },
                        onOrderPlaced = { order ->
                            navController.navigate("tracking/${order.orderId}") {
                                popUpTo("home")
                            }
                        }
                    )
                }

                // Order Tracking & Confirmation
                composable(
                    route = "tracking/{orderId}",
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId")
                    OrderTrackingScreen(
                        initialOrderId = orderId,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onExploreClick = { navController.navigate("catalogue") }
                    )
                }

                // Care Guides & Reminders
                composable("care") {
                    CareGuidesScreen(
                        viewModel = viewModel,
                        onNavigateToReminders = { navController.navigate("reminders") }
                    )
                }

                // Care Reminders
                composable("reminders") {
                    CareRemindersScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Customer Profile & Dashboard
                composable("dashboard") {
                    CustomerDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToOrders = { navController.navigate("tracking/ALL") },
                        onNavigateToWishlist = { navController.navigate("wishlist") },
                        onNavigateToReminders = { navController.navigate("reminders") },
                        onNavigateToQuiz = { navController.navigate("quiz") }
                    )
                }

                // Admin Dashboard
                composable("admin") {
                    AdminDashboardScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            viewModel.toggleAdminMode()
                            navController.navigate("home") {
                                popUpTo("admin") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
