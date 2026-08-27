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

enum class DeliveryOption(val displayName: String, val fee: Double, val estimatedTime: String) {
    STANDARD_DELIVERY("Standard Delivery", 99.0, "3-5 Business Days"),
    EXPRESS_DELIVERY("Express Delivery", 199.0, "1-2 Business Days"),
    STORE_PICKUP("Store Pickup – Diva East (Pickup from our Diva East store)", 0.0, "Ready in 2 Hours")
}

enum class PaymentMethod(val displayName: String) {
    UPI("UPI (Google Pay, PhonePe, Paytm, BHIM)"),
    CASH_ON_DELIVERY("Cash on Delivery (COD)"),
    CARD("Debit / Credit Card (Visa, RuPay, MasterCard)"),
    NET_BANKING("Net Banking (SBI, HDFC, ICICI, Axis)")
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
    val itemsSummary: String = "",
    val state: String = "Maharashtra",
    val flatHouse: String = "",
    val street: String = "",
    val locality: String = "",
    val landmark: String = ""
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

