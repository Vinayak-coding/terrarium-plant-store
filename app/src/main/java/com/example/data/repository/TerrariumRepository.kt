package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.model.BundleKit
import com.example.data.model.CareGuide
import com.example.data.model.CareReminder
import com.example.data.model.CartItem
import com.example.data.model.DeliveryOption
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.QuizPreferences
import com.example.data.model.QuizRecommendationResult
import com.example.data.model.ReminderType
import com.example.data.model.Review
import com.example.data.model.UserAccount
import com.example.data.model.WishlistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TerrariumRepository(private val database: AppDatabase) {
    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val wishlistDao = database.wishlistDao()
    private val orderDao = database.orderDao()
    private val reminderDao = database.reminderDao()
    private val reviewDao = database.reviewDao()

    // Products
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val featuredProducts: Flow<List<Product>> = productDao.getFeaturedProducts()
    val newArrivals: Flow<List<Product>> = productDao.getNewArrivals()
    val bestSellers: Flow<List<Product>> = productDao.getBestSellers()

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    suspend fun addProduct(product: Product): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    // Cart
    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems()

    suspend fun addToCart(productId: Long, quantity: Int = 1) {
        val currentItems = cartDao.getCartItems().first()
        val existing = currentItems.find { it.productId == productId }
        if (existing != null) {
            cartDao.updateQuantity(existing.id, existing.quantity + quantity)
        } else {
            cartDao.insertCartItem(CartItem(productId = productId, quantity = quantity))
        }
    }

    suspend fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, quantity)
        }
    }

    suspend fun removeFromCart(cartItemId: Long) = cartDao.deleteCartItem(cartItemId)
    suspend fun clearCart() = cartDao.clearCart()

    // Wishlist
    val wishlistItems: Flow<List<WishlistItem>> = wishlistDao.getWishlistItems()

    suspend fun toggleWishlist(productId: Long) {
        val current = wishlistDao.getWishlistItems().first()
        if (current.any { it.productId == productId }) {
            wishlistDao.removeFromWishlist(productId)
        } else {
            wishlistDao.addToWishlist(WishlistItem(productId = productId))
        }
    }

    fun isInWishlist(productId: Long): Flow<Boolean> = wishlistDao.isInWishlist(productId)

    // Orders
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun getOrderById(orderId: String): Order? = orderDao.getOrderById(orderId)

    suspend fun placeDemoOrder(
        user: UserAccount,
        cartProducts: List<Pair<Product, Int>>,
        deliveryOption: DeliveryOption,
        paymentMethod: PaymentMethod,
        couponCode: String? = null
    ): Order {
        val randomNum = Random.nextInt(10000, 99999)
        val orderId = "TER-$randomNum"
        
        val subtotal = cartProducts.sumOf { (product, qty) ->
            (product.discountPrice ?: product.price) * qty
        }
        
        val discount = if (couponCode?.trim()?.uppercase() == "GROW20") {
            subtotal * 0.20
        } else if (couponCode?.trim()?.uppercase() == "PLANT10") {
            subtotal * 0.10
        } else 0.0

        val deliveryFee = if (couponCode?.trim()?.uppercase() == "FREESHIP" || subtotal >= 999.0 || deliveryOption == DeliveryOption.STORE_PICKUP) {
            0.0
        } else {
            deliveryOption.fee
        }

        val totalAmount = maxOf(0.0, subtotal - discount + deliveryFee)

        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale("en", "IN"))
        val expectedDate = dateFormat.format(Date(System.currentTimeMillis() + (86400000L * 3)))

        val itemsSummary = cartProducts.joinToString(", ") { "${it.first.name} (x${it.second})" }

        val order = Order(
            orderId = orderId,
            customerName = user.name,
            phone = user.phone,
            email = user.email,
            address = user.defaultAddress,
            city = user.city,
            postalCode = user.pinCode,
            state = user.state,
            flatHouse = user.flatHouse,
            street = user.street,
            locality = user.locality,
            landmark = user.landmark,
            deliveryOption = deliveryOption,
            paymentMethod = paymentMethod,
            subtotal = subtotal,
            discount = discount,
            deliveryFee = deliveryFee,
            totalAmount = totalAmount,
            status = OrderStatus.ORDER_PLACED,
            createdAt = System.currentTimeMillis(),
            expectedDeliveryDate = expectedDate,
            couponApplied = couponCode,
            itemsSummary = itemsSummary
        )

        // Insert order & items
        orderDao.insertOrder(order)
        val orderItems = cartProducts.map { (product, qty) ->
            OrderItem(
                orderId = orderId,
                productId = product.id,
                productName = product.name,
                productPrice = product.discountPrice ?: product.price,
                quantity = qty
            )
        }
        orderDao.insertOrderItems(orderItems)

        // Decrease product stock & automatically add care reminders for ordered plants
        for ((product, qty) in cartProducts) {
            productDao.decreaseStock(product.id, qty)
            if (product.isPlant) {
                reminderDao.insertReminder(
                    CareReminder(
                        plantName = product.name,
                        plantType = product.category.displayName,
                        reminderType = ReminderType.WATERING,
                        frequencyDays = 7,
                        nextDueDateMillis = System.currentTimeMillis() + (86400000L * 7),
                        notes = "Water thoroughly when top soil dries."
                    )
                )
            }
        }

        // Clear cart
        cartDao.clearCart()
        return order
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    // Reminders
    val allReminders: Flow<List<CareReminder>> = reminderDao.getAllReminders()

    suspend fun addReminder(reminder: CareReminder) = reminderDao.insertReminder(reminder)
    suspend fun updateReminder(reminder: CareReminder) = reminderDao.updateReminder(reminder)
    suspend fun markReminderCompleted(id: Long) = reminderDao.markCompleted(id)
    suspend fun deleteReminder(id: Long) = reminderDao.deleteReminder(id)

    // Reviews
    fun getReviewsForProduct(productId: Long): Flow<List<Review>> = reviewDao.getReviewsForProduct(productId)
    val allReviews: Flow<List<Review>> = reviewDao.getAllReviews()

    suspend fun addReview(review: Review) = reviewDao.insertReview(review)
    suspend fun updateReviewApproval(id: Long, approved: Boolean) = reviewDao.updateReviewApproval(id, approved)

    // Bundles & Guides (Seed Collections)
    val bundleKits: List<BundleKit> = SeedData.bundleKits
    val careGuides: List<CareGuide> = SeedData.careGuides

    fun getBundleById(id: String): BundleKit? = SeedData.bundleKits.find { it.id == id }
    fun getCareGuideById(id: String): CareGuide? = SeedData.careGuides.find { it.id == id }

    // Plant Match Quiz Rule-Based Engine
    suspend fun calculatePlantMatch(answers: QuizPreferences): List<QuizRecommendationResult> {
        val products = productDao.getAllProducts().first().filter { it.isPlant }
        if (products.isEmpty()) return emptyList()

        val scoredResults = products.map { plant ->
            var score = 50 // Base score
            val reasons = mutableListOf<String>()

            // 1. Space check
            if (answers.space.isNotBlank()) {
                if (plant.suitableSpaces.contains(answers.space, ignoreCase = true)) {
                    score += 20
                    reasons.add("Specially suited for your ${answers.space}")
                }
            }

            // 2. Sunlight check
            if (answers.sunlight.isNotBlank()) {
                when {
                    answers.sunlight.contains("Low", ignoreCase = true) &&
                            plant.sunlight.displayName.contains("Low", ignoreCase = true) -> {
                        score += 15
                        reasons.add("Thrives in low/ambient light")
                    }
                    answers.sunlight.contains("Bright", ignoreCase = true) &&
                            (plant.sunlight.displayName.contains("Bright", ignoreCase = true) || plant.sunlight.displayName.contains("Direct", ignoreCase = true)) -> {
                        score += 15
                        reasons.add("Loves your bright sunny area")
                    }
                    else -> {
                        score += 10
                        reasons.add("Adaptable to indoor conditions")
                    }
                }
            }

            // 3. Care Frequency
            if (answers.careFrequency.isNotBlank()) {
                if (answers.careFrequency.contains("Rarely", ignoreCase = true)) {
                    if (plant.wateringFrequency.contains("2-3 weeks", ignoreCase = true) || plant.wateringFrequency.contains("3-4 weeks", ignoreCase = true)) {
                        score += 15
                        reasons.add("Drought tolerant for busy schedules")
                    }
                } else if (answers.careFrequency.contains("Frequently", ignoreCase = true)) {
                    if (plant.maintenance == com.example.data.model.MaintenanceLevel.MODERATE || plant.category == ProductCategory.HERBS) {
                        score += 15
                        reasons.add("Rewards frequent attention & care")
                    }
                }
            }

            // 4. Experience level
            if (answers.experienceLevel.contains("Beginner", ignoreCase = true) && plant.maintenance == com.example.data.model.MaintenanceLevel.VERY_EASY) {
                score += 10
                reasons.add("Zero-stress beginner friendly")
            }

            // 5. Purpose
            if (answers.purpose.isNotBlank()) {
                if (answers.purpose.contains("Air", ignoreCase = true) && plant.benefits.contains("Air", ignoreCase = true)) {
                    score += 10
                    reasons.add("Popular indoor air-freshening foliage")
                } else if (answers.purpose.contains("Pet", ignoreCase = true) && plant.isPetFriendly) {
                    score += 15
                    reasons.add("Pet friendly variety")
                } else if (answers.purpose.contains("Herb", ignoreCase = true) && plant.category == ProductCategory.HERBS) {
                    score += 15
                    reasons.add("Aromatic culinary harvest")
                }
            }

            val finalMatch = minOf(99, maxOf(70, score))
            val mainReason = if (reasons.isNotEmpty()) reasons.joinToString(" • ") else "Great general match for your lifestyle"

            // Match recommended bundle
            val bundle = when {
                plant.id == 1L || plant.id == 6L -> bundleKits.find { it.id == "kit_low_light" }
                plant.category == ProductCategory.HERBS -> bundleKits.find { it.id == "kit_herb" }
                plant.category == ProductCategory.SUCCULENTS -> bundleKits.find { it.id == "kit_succulent" }
                else -> bundleKits.find { it.id == "kit_beginner" }
            }

            QuizRecommendationResult(
                product = plant,
                matchPercentage = finalMatch,
                matchReason = mainReason,
                recommendedBundle = bundle
            )
        }

        return scoredResults.sortedByDescending { it.matchPercentage }
    }
}
