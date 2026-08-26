package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.BundleKit
import com.example.data.model.CareGuide
import com.example.data.model.CareReminder
import com.example.data.model.CartItem
import com.example.data.model.DeliveryOption
import com.example.data.model.MaintenanceLevel
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentMethod
import com.example.data.model.PlantSize
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.QuizPreferences
import com.example.data.model.QuizRecommendationResult
import com.example.data.model.ReminderType
import com.example.data.model.Review
import com.example.data.model.SpaceCategory
import com.example.data.model.SunlightRequirement
import com.example.data.model.UserAccount
import com.example.data.model.WishlistItem
import com.example.data.repository.TerrariumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ProductSortOption(val displayName: String) {
    POPULARITY("Most Popular"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Highest Rated"),
    NEWEST("New Arrivals")
}

data class FilterState(
    val searchQuery: String = "",
    val category: ProductCategory? = null,
    val minPrice: Double = 0.0,
    val maxPrice: Double = 100.0,
    val plantSize: PlantSize? = null,
    val sunlight: SunlightRequirement? = null,
    val maintenance: MaintenanceLevel? = null,
    val space: SpaceCategory? = null,
    val minRating: Float = 0f,
    val sortOption: ProductSortOption = ProductSortOption.POPULARITY,
    val isGridView: Boolean = true
)

class TerrariumViewModel(private val repository: TerrariumRepository) : ViewModel() {

    // User Account & Role State
    private val _currentUser = MutableStateFlow(UserAccount())
    val currentUser: StateFlow<UserAccount> = _currentUser.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun loginUser(name: String, email: String) {
        _currentUser.value = _currentUser.value.copy(
            name = name.ifBlank { "Green Thumb" },
            email = email.ifBlank { "plantlover@terrarium.com" }
        )
    }

    fun updateUserProfile(name: String, email: String, phone: String, address: String, city: String, postal: String) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            email = email,
            phone = phone,
            defaultAddress = address,
            city = city,
            postalCode = postal
        )
    }

    // Products & Filtering
    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<Product>> = repository.featuredProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val newArrivals: StateFlow<List<Product>> = repository.newArrivals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestSellers: StateFlow<List<Product>> = repository.bestSellers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        _filterState
    ) { products, filter ->
        var list = products.filter { product ->
            val matchesQuery = filter.searchQuery.isBlank() ||
                    product.name.contains(filter.searchQuery, ignoreCase = true) ||
                    product.category.displayName.contains(filter.searchQuery, ignoreCase = true) ||
                    product.shortDescription.contains(filter.searchQuery, ignoreCase = true)

            val matchesCategory = filter.category == null || product.category == filter.category
            val price = product.discountPrice ?: product.price
            val matchesPrice = price in filter.minPrice..filter.maxPrice
            val matchesSize = filter.plantSize == null || product.plantSize == filter.plantSize
            val matchesSunlight = filter.sunlight == null || product.sunlight == filter.sunlight
            val matchesMaintenance = filter.maintenance == null || product.maintenance == filter.maintenance
            val matchesSpace = filter.space == null || product.suitableSpaces.contains(filter.space.displayName, ignoreCase = true)
            val matchesRating = product.rating >= filter.minRating

            matchesQuery && matchesCategory && matchesPrice && matchesSize &&
                    matchesSunlight && matchesMaintenance && matchesSpace && matchesRating
        }

        // Sorting
        list = when (filter.sortOption) {
            ProductSortOption.POPULARITY -> list.sortedByDescending { it.reviewCount }
            ProductSortOption.PRICE_LOW_HIGH -> list.sortedBy { it.discountPrice ?: it.price }
            ProductSortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.discountPrice ?: it.price }
            ProductSortOption.RATING -> list.sortedByDescending { it.rating }
            ProductSortOption.NEWEST -> list.sortedByDescending { it.isNewArrival }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun setCategoryFilter(category: ProductCategory?) {
        _filterState.value = _filterState.value.copy(category = category)
    }

    fun setSpaceFilter(space: SpaceCategory?) {
        _filterState.value = _filterState.value.copy(space = space)
    }

    fun setSortOption(sort: ProductSortOption) {
        _filterState.value = _filterState.value.copy(sortOption = sort)
    }

    fun toggleViewMode() {
        _filterState.value = _filterState.value.copy(isGridView = !_filterState.value.isGridView)
    }

    fun resetFilters() {
        _filterState.value = FilterState()
    }

    // Cart & Wishlist
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItem>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addToCart(productId: Long, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }

    fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, quantity)
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun toggleWishlist(productId: Long) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    // Coupon & Checkout State
    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError: StateFlow<String?> = _couponError.asStateFlow()

    fun applyCoupon(code: String) {
        val clean = code.trim().uppercase()
        when (clean) {
            "GROW20" -> {
                _appliedCoupon.value = "GROW20"
                _couponError.value = null
            }
            "PLANT10" -> {
                _appliedCoupon.value = "PLANT10"
                _couponError.value = null
            }
            "FREESHIP" -> {
                _appliedCoupon.value = "FREESHIP"
                _couponError.value = null
            }
            else -> {
                _couponError.value = "Invalid coupon code. Try GROW20 or FREESHIP"
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponError.value = null
    }

    // Placing Demo Order
    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    fun placeOrder(
        cartProducts: List<Pair<Product, Int>>,
        deliveryOption: DeliveryOption,
        paymentMethod: PaymentMethod,
        onSuccess: (Order) -> Unit
    ) {
        viewModelScope.launch {
            val order = repository.placeDemoOrder(
                user = _currentUser.value,
                cartProducts = cartProducts,
                deliveryOption = deliveryOption,
                paymentMethod = paymentMethod,
                couponCode = _appliedCoupon.value
            )
            _lastPlacedOrder.value = order
            _appliedCoupon.value = null
            onSuccess(order)
        }
    }

    // Orders History & Tracking
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateOrderStatus(orderId: String, nextStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, nextStatus)
        }
    }

    // Plant Match Quiz State
    private val _quizStep = MutableStateFlow(0)
    val quizStep: StateFlow<Int> = _quizStep.asStateFlow()

    private val _quizPreferences = MutableStateFlow(QuizPreferences())
    val quizPreferences: StateFlow<QuizPreferences> = _quizPreferences.asStateFlow()

    private val _quizRecommendations = MutableStateFlow<List<QuizRecommendationResult>>(emptyList())
    val quizRecommendations: StateFlow<List<QuizRecommendationResult>> = _quizRecommendations.asStateFlow()

    fun setQuizAnswer(key: String, value: String) {
        val current = _quizPreferences.value
        _quizPreferences.value = when (key) {
            "space" -> current.copy(space = value)
            "sunlight" -> current.copy(sunlight = value)
            "careFrequency" -> current.copy(careFrequency = value)
            "experienceLevel" -> current.copy(experienceLevel = value)
            "plantSize" -> current.copy(plantSize = value)
            "budget" -> current.copy(budget = value)
            "purpose" -> current.copy(purpose = value)
            else -> current
        }
    }

    fun nextQuizStep() {
        if (_quizStep.value < 6) {
            _quizStep.value = _quizStep.value + 1
        } else {
            submitQuiz()
        }
    }

    fun previousQuizStep() {
        if (_quizStep.value > 0) {
            _quizStep.value = _quizStep.value - 1
        }
    }

    fun resetQuiz() {
        _quizStep.value = 0
        _quizPreferences.value = QuizPreferences()
        _quizRecommendations.value = emptyList()
    }

    fun submitQuiz() {
        viewModelScope.launch {
            val results = repository.calculatePlantMatch(_quizPreferences.value)
            _quizRecommendations.value = results
            _quizStep.value = 7 // Results view
        }
    }

    // Bundles & Guides
    val bundleKits: List<BundleKit> = repository.bundleKits
    val careGuides: List<CareGuide> = repository.careGuides

    fun addBundleToCart(bundle: BundleKit) {
        viewModelScope.launch {
            bundle.includedItemIds.forEach { itemId ->
                repository.addToCart(itemId, 1)
            }
        }
    }

    // Care Reminders
    val allReminders: StateFlow<List<CareReminder>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCareReminder(plantName: String, plantType: String, type: ReminderType, frequencyDays: Int, notes: String) {
        viewModelScope.launch {
            repository.addReminder(
                CareReminder(
                    plantName = plantName,
                    plantType = plantType,
                    reminderType = type,
                    frequencyDays = frequencyDays,
                    nextDueDateMillis = System.currentTimeMillis() + (86400000L * frequencyDays),
                    notes = notes
                )
            )
        }
    }

    fun markReminderCompleted(id: Long) {
        viewModelScope.launch {
            repository.markReminderCompleted(id)
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            repository.deleteReminder(id)
        }
    }

    // Product Reviews
    fun getProductReviews(productId: Long): Flow<List<Review>> = repository.getReviewsForProduct(productId)
    val allReviews: StateFlow<List<Review>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addReview(productId: Long, author: String, rating: Int, comment: String, setupPhoto: String?) {
        viewModelScope.launch {
            repository.addReview(
                Review(
                    productId = productId,
                    customerName = author.ifBlank { "Botanical Enthusiast" },
                    rating = rating,
                    comment = comment,
                    dateText = "Today",
                    plantSetupPhoto = setupPhoto,
                    isApproved = true
                )
            )
        }
    }

    fun updateReviewApproval(id: Long, approved: Boolean) {
        viewModelScope.launch {
            repository.updateReviewApproval(id, approved)
        }
    }

    // Admin Inventory Management
    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.addProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}

class TerrariumViewModelFactory(private val repository: TerrariumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TerrariumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TerrariumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
