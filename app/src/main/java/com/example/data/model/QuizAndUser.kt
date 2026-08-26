package com.example.data.model

data class QuizQuestion(
    val step: Int,
    val title: String,
    val subtitle: String,
    val options: List<QuizOption>
)

data class QuizOption(
    val id: String,
    val label: String,
    val description: String,
    val iconKey: String
)

data class QuizPreferences(
    val space: String = "",
    val sunlight: String = "",
    val careFrequency: String = "",
    val experienceLevel: String = "",
    val plantSize: String = "",
    val budget: String = "",
    val purpose: String = ""
)

data class QuizRecommendationResult(
    val product: Product,
    val matchPercentage: Int,
    val matchReason: String,
    val recommendedBundle: BundleKit? = null
)

data class UserAccount(
    val name: String = "Vinayak Lavhate",
    val email: String = "vinayak@example.com",
    val phone: String = "+1 (555) 349-2810",
    val defaultAddress: String = "742 Evergreen Botanical Way",
    val city: String = "San Francisco",
    val postalCode: String = "94107",
    val isGuest: Boolean = false,
    val isAdmin: Boolean = false
)
