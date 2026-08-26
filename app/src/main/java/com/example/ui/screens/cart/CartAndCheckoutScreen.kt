package com.example.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryOption
import com.example.data.model.Order
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AlertRed
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaLight
import com.example.ui.viewmodel.TerrariumViewModel

@Composable
fun CartAndCheckoutScreen(
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    onExploreClick: () -> Unit,
    onOrderPlaced: (Order) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponError by viewModel.couponError.collectAsState()

    // Form inputs initialized from user profile
    var nameInput by remember(currentUser) { mutableStateOf(currentUser.name) }
    var phoneInput by remember(currentUser) { mutableStateOf(currentUser.phone) }
    var emailInput by remember(currentUser) { mutableStateOf(currentUser.email) }
    var addressInput by remember(currentUser) { mutableStateOf(currentUser.defaultAddress) }
    var cityInput by remember(currentUser) { mutableStateOf(currentUser.city) }
    var postalInput by remember(currentUser) { mutableStateOf(currentUser.postalCode) }

    var deliveryOption by remember { mutableStateOf(DeliveryOption.STANDARD_DELIVERY) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.ONLINE_CARD_UPI) }
    var couponInput by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Map cart items to products with quantities
    val cartProducts = cartItems.mapNotNull { item ->
        val product = allProducts.find { it.id == item.productId }
        if (product != null) Pair(product, item.quantity) else null
    }

    val subtotal = cartProducts.sumOf { (prod, qty) ->
        (prod.discountPrice ?: prod.price) * qty
    }

    val discountAmount = when (appliedCoupon) {
        "GROW20" -> subtotal * 0.20
        "PLANT10" -> subtotal * 0.10
        else -> 0.0
    }

    val deliveryFee = if (appliedCoupon == "FREESHIP" || subtotal >= 40.0 || deliveryOption == DeliveryOption.STORE_PICKUP) {
        0.0
    } else {
        deliveryOption.fee
    }

    val grandTotal = maxOf(0.0, subtotal - discountAmount + deliveryFee)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (cartProducts.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Cart",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                }

                EmptyStateView(
                    title = "Your shopping cart is empty",
                    message = "Add fresh indoor plants, ceramic pots, or complete care kits to get started.",
                    icon = Icons.Default.ShoppingBag,
                    actionButtonText = "Explore Catalogue",
                    onActionClick = onExploreClick
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ForestGreenDark)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Shopping Cart (${cartProducts.sumOf { it.second }})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                }

                // 1. Cart Items List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    cartItems.forEach { item ->
                        val product = allProducts.find { it.id == item.productId }
                        if (product != null) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    ) {
                                        BotanicalIllustration(
                                            imageIndex = product.imageIndex,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenDark,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "$${String.format("%.2f", product.discountPrice ?: product.price)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Qty buttons
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SoftSageContainer)
                                        ) {
                                            IconButton(
                                                onClick = { viewModel.updateCartQuantity(item.id, item.quantity - 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(14.dp))
                                            }
                                            Text(
                                                text = "${item.quantity}",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                            IconButton(
                                                onClick = { viewModel.updateCartQuantity(item.id, item.quantity + 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }

                                    IconButton(onClick = { viewModel.removeFromCart(item.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Coupon Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Discount, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Have a Promo Code?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (appliedCoupon != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MintLight)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Coupon $appliedCoupon applied!",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenDark
                                    )
                                }
                                IconButton(onClick = { viewModel.removeCoupon() }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove coupon", tint = AlertRed)
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponInput,
                                    onValueChange = { couponInput = it },
                                    placeholder = { Text("e.g. GROW20, FREESHIP") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        viewModel.applyCoupon(couponInput)
                                        couponInput = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Apply")
                                }
                            }
                            if (couponError != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = couponError ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AlertRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Checkout Form (Shipping & Contact)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Delivery Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Full Name *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { phoneInput = it },
                                label = { Text("Phone *") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                label = { Text("Email *") },
                                singleLine = true,
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        OutlinedTextField(
                            value = addressInput,
                            onValueChange = { addressInput = it },
                            label = { Text("Delivery Address *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cityInput,
                                onValueChange = { cityInput = it },
                                label = { Text("City") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = postalInput,
                                onValueChange = { postalInput = it },
                                label = { Text("Postal Code") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Delivery Method Selection
                        Text(
                            text = "Delivery Method",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (deliveryOption == DeliveryOption.STANDARD_DELIVERY) MintLight else Color.Transparent)
                                .padding(4.dp)
                        ) {
                            RadioButton(
                                selected = deliveryOption == DeliveryOption.STANDARD_DELIVERY,
                                onClick = { deliveryOption = DeliveryOption.STANDARD_DELIVERY }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("Standard Plant-Safe Delivery ($5.00)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                Text("Delivered in 2-3 business days (Free over $40)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (deliveryOption == DeliveryOption.STORE_PICKUP) MintLight else Color.Transparent)
                                .padding(4.dp)
                        ) {
                            RadioButton(
                                selected = deliveryOption == DeliveryOption.STORE_PICKUP,
                                onClick = { deliveryOption = DeliveryOption.STORE_PICKUP }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("In-Store Nursery Pickup (FREE)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                Text("Ready in 2 hours at Terrarium Urban Greenhouse", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Payment Method Selection
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.ONLINE_CARD_UPI,
                                onClick = { paymentMethod = PaymentMethod.ONLINE_CARD_UPI }
                            )
                            Text("Online Card / UPI (Demo Flow)", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.CASH_ON_DELIVERY,
                                onClick = { paymentMethod = PaymentMethod.CASH_ON_DELIVERY }
                            )
                            Text("Cash on Delivery / Pickup", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Order Summary & Totals
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Order Calculation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                            Text("$${String.format("%.2f", subtotal)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }

                        if (discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Discount ($appliedCoupon)", style = MaterialTheme.typography.bodyMedium, color = ForestGreenPrimary)
                                Text("-$${String.format("%.2f", discountAmount)}", style = MaterialTheme.typography.bodyMedium, color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Delivery Fee", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                if (deliveryFee == 0.0) "FREE" else "$${String.format("%.2f", deliveryFee)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Grand Total",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                            Text(
                                text = "$${String.format("%.2f", grandTotal)}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        }

                        // Demo Notice Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoftSageContainer)
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Demo Checkout Mode — No payment charge will be incurred.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestGreenDark
                                )
                            }
                        }

                        if (validationError != null) {
                            Text(
                                text = validationError ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = AlertRed
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Place Demo Order Button
                        Button(
                            onClick = {
                                if (nameInput.isBlank() || phoneInput.isBlank() || emailInput.isBlank() || addressInput.isBlank()) {
                                    validationError = "Please fill in your name, phone, email and address."
                                } else {
                                    viewModel.updateUserProfile(nameInput, emailInput, phoneInput, addressInput, cityInput, postalInput)
                                    viewModel.placeOrder(
                                        cartProducts = cartProducts,
                                        deliveryOption = deliveryOption,
                                        paymentMethod = paymentMethod,
                                        onSuccess = { order ->
                                            onOrderPlaced(order)
                                        }
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TerracottaAccent),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("cart_place_order_btn")
                        ) {
                            Text(
                                text = "Place Demo Order ($${String.format("%.2f", grandTotal)})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
