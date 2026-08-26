package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    ORDER_PLACED("Order Placed", 0),
    ORDER_CONFIRMED("Order Confirmed", 1),
    PACKED("Packed", 2),
    DISPATCHED("Dispatched", 3),
    OUT_FOR_DELIVERY("Out for Delivery", 4),
    DELIVERED("Delivered", 5)
}

enum class DeliveryOption(val displayName: String, val fee: Double) {
    STANDARD_DELIVERY("Standard Eco Delivery", 5.0),
    STORE_PICKUP("Terrarium Store Pickup (Free)", 0.0)
}

enum class PaymentMethod(val displayName: String) {
    ONLINE_CARD_UPI("Demo Online Card / UPI"),
    CASH_ON_DELIVERY("Cash on Delivery")
}

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String,
    val customerName: String,
    val phone: String,
    val email: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val deliveryOption: DeliveryOption,
    val paymentMethod: PaymentMethod,
    val subtotal: Double,
    val discount: Double,
    val deliveryFee: Double,
    val totalAmount: Double,
    val status: OrderStatus = OrderStatus.ORDER_PLACED,
    val createdAt: Long = System.currentTimeMillis(),
    val expectedDeliveryDate: String,
    val couponApplied: String? = null,
    val itemsSummary: String = ""
)

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: String,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val quantity: Int
)
