package com.example.data.model

data class BundleKit(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val originalPrice: Double,
    val bundlePrice: Double,
    val discountPercent: Int,
    val includedItemIds: List<Long>,
    val includedItemNames: List<String>,
    val tag: String,
    val iconType: String
)
