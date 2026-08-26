package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProductCategory(val displayName: String) {
    INDOOR_PLANTS("Indoor Plants"),
    FLOWERING_PLANTS("Flowering Plants"),
    SUCCULENTS("Succulents"),
    HERBS("Herbs & Edibles"),
    LOW_LIGHT("Low-Light Plants"),
    AIR_PURIFYING("Air Purifying"),
    POTS_PLANTERS("Pots & Planters"),
    SOIL_FERTILIZER("Soil & Fertilizers"),
    TOOLS_ACCESSORIES("Tools & Care Kits")
}

enum class PlantSize(val displayName: String) {
    SMALL("Small (4-6\")"),
    MEDIUM("Medium (8-10\")"),
    LARGE("Large (12-16\")"),
    NOT_APPLICABLE("N/A")
}

enum class SunlightRequirement(val displayName: String) {
    LOW_INDIRECT("Low Indirect Light"),
    MEDIUM_INDIRECT("Medium Indirect Light"),
    BRIGHT_INDIRECT("Bright Indirect Light"),
    DIRECT_SUN("Direct Sunlight"),
    ANY("Adaptable / Any")
}

enum class MaintenanceLevel(val displayName: String) {
    VERY_EASY("Very Easy"),
    EASY("Easy"),
    MODERATE("Moderate"),
    HIGH("High")
}

enum class SpaceCategory(val displayName: String, val description: String) {
    BEDROOM("Bedroom", "Calming, oxygen-rich plants suited for gentle sleep environments"),
    LIVING_ROOM("Living Room", "Statement foliage and lush greenery for vibrant social spaces"),
    BALCONY("Balcony", "Sun-loving, flowering and hardy balcony container plants"),
    OFFICE("Office Desk", "Compact, low-maintenance greens that boost focus and purify air"),
    KITCHEN("Kitchen", "Aromatic fresh cooking herbs and moisture-friendly greenery"),
    BATHROOM("Bathroom", "Humidity-loving tropical ferns and moisture absorbers"),
    SMALL_SPACES("Small Spaces", "Space-saving trailing vines and micro terrariums"),
    OUTDOOR_GARDEN("Outdoor Garden", "Hardy outdoor shrubs, decorative planters and perennial blooms")
}

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: ProductCategory,
    val price: Double,
    val discountPrice: Double? = null,
    val rating: Float = 4.8f,
    val reviewCount: Int = 12,
    val stock: Int = 25,
    val shortDescription: String,
    val description: String,
    val tagLabel: String? = null, // "Best for Beginners", "Low Maintenance", "Pet Friendly"
    val plantSize: PlantSize = PlantSize.MEDIUM,
    val potSize: String = "6 inch Nursery Pot",
    val sunlight: SunlightRequirement = SunlightRequirement.MEDIUM_INDIRECT,
    val wateringFrequency: String = "Once every 7-10 days",
    val maintenance: MaintenanceLevel = MaintenanceLevel.EASY,
    val isIndoor: Boolean = true,
    val isOutdoor: Boolean = false,
    val isPetFriendly: Boolean = true,
    val benefits: String = "Air purifying • Stress reduction • Aesthetic focal point",
    val suitableSpaces: String = "Living Room, Bedroom, Office",
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val isBestSeller: Boolean = false,
    val isPlant: Boolean = true,
    val specifications: String = "Material: Ceramic with drainage hole; Dimensions: 6x6x6 inches",
    val careInstructions: String = "Allow top 2 inches of soil to dry before thorough watering. Keep away from cold drafts.",
    val imageIndex: Int = 0 // For custom botanical vector rendering
)
