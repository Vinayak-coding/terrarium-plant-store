package com.example.data.model

import java.text.NumberFormat
import java.util.Locale

val IndianStatesAndUTs = listOf(
    "Andaman and Nicobar Islands",
    "Andhra Pradesh",
    "Arunachal Pradesh",
    "Assam",
    "Bihar",
    "Chandigarh",
    "Chhattisgarh",
    "Dadra and Nagar Haveli and Daman and Diu",
    "Delhi (NCT)",
    "Goa",
    "Gujarat",
    "Haryana",
    "Himachal Pradesh",
    "Jammu and Kashmir",
    "Jharkhand",
    "Karnataka",
    "Kerala",
    "Ladakh",
    "Lakshadweep",
    "Madhya Pradesh",
    "Maharashtra",
    "Manipur",
    "Meghalaya",
    "Mizoram",
    "Nagaland",
    "Odisha",
    "Puducherry",
    "Punjab",
    "Rajasthan",
    "Sikkim",
    "Tamil Nadu",
    "Telangana",
    "Tripura",
    "Uttar Pradesh",
    "Uttarakhand",
    "West Bengal"
)

fun formatRupees(amount: Double): String {
    val rounded = amount.toLong()
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
    val formatted = formatter.format(amount)
    // Ensure clean ₹ symbol format
    return if (formatted.startsWith("₹") || formatted.startsWith("Rs.")) {
        if (formatted.startsWith("Rs.")) "₹" + formatted.removePrefix("Rs.").trim() else formatted
    } else {
        "₹$rounded"
    }
}

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
    val name: String = "New Customer",
    val email: String = "customer@example.com",
    val phone: String = "+91 98200 12345",
    val flatHouse: String = "Flat 402, Nilgiri Heights",
    val street: String = "Pokhran Road No. 2",
    val locality: String = "Vartak Nagar",
    val landmark: String = "Near Upvan Lake",
    val city: String = "Thane",
    val state: String = "Maharashtra",
    val pinCode: String = "400606",
    val isGuest: Boolean = false,
    val isAdmin: Boolean = false,
    val membershipTier: String = "New Plant Parent"
) {
    val defaultAddress: String
        get() = "$flatHouse, $street, $locality, $city, $state - $pinCode"
    val address: String
        get() = defaultAddress
    val postalCode: String
        get() = pinCode
}

data class AdminProfile(
    val name: String = "Store Admin",
    val role: String = "Store Administrator",
    val storeLocation: String = "Thane, Maharashtra",
    val storeName: String = "[Store Name]",
    val storeAddress: String = "[Store Address, Thane, Maharashtra]",
    val isDemoAdmin: Boolean = true
)

