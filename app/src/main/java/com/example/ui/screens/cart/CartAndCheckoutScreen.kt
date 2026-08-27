package com.example.ui.screens.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryOption
import com.example.data.model.IndianStatesAndUTs
import com.example.data.model.Order
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.model.formatRupees
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AlertRed
import com.example.ui.theme.ClayBorder
import com.example.ui.theme.DeepNeem
import com.example.ui.theme.MangoLeaf
import com.example.ui.theme.ParrotGreen
import com.example.ui.theme.PistachioMist
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaLight
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.TerrariumViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    var flatHouseInput by remember(currentUser) { mutableStateOf(currentUser.flatHouse) }
    var streetInput by remember(currentUser) { mutableStateOf(currentUser.street) }
    var localityInput by remember(currentUser) { mutableStateOf(currentUser.locality) }
    var landmarkInput by remember(currentUser) { mutableStateOf(currentUser.landmark) }
    var cityInput by remember(currentUser) { mutableStateOf(currentUser.city) }
    var stateInput by remember(currentUser) { mutableStateOf(currentUser.state) }
    var pinCodeInput by remember(currentUser) { mutableStateOf(currentUser.pinCode) }

    var stateDropdownExpanded by remember { mutableStateOf(false) }

    var deliveryOption by remember { mutableStateOf(DeliveryOption.STANDARD_DELIVERY) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.UPI) }
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

    val isFreeDelivery = appliedCoupon == "FREESHIP" || subtotal >= 999.0 || deliveryOption == DeliveryOption.STORE_PICKUP
    val deliveryFee = if (isFreeDelivery) 0.0 else deliveryOption.fee

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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepNeem)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Cart",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepNeem
                    )
                }

                EmptyStateView(
                    title = "Your shopping cart is empty",
                    message = "Add live indoor plants, terracotta planters, or complete plant-care kits to begin.",
                    icon = Icons.Default.ShoppingBag,
                    actionButtonText = "Explore Plants",
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepNeem)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Shopping Cart (${cartProducts.sumOf { it.second }} items)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DeepNeem
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
                                border = BorderStroke(1.dp, ClayBorder.copy(alpha = 0.6f)),
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
                                            color = DeepNeem,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = formatRupees(product.discountPrice ?: product.price),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ParrotGreen
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Qty buttons
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(PistachioMist)
                                        ) {
                                            IconButton(
                                                onClick = { viewModel.updateCartQuantity(item.id, item.quantity - 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(14.dp), tint = DeepNeem)
                                            }
                                            Text(
                                                text = "${item.quantity}",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepNeem,
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                            IconButton(
                                                onClick = { viewModel.updateCartQuantity(item.id, item.quantity + 1) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(14.dp), tint = DeepNeem)
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
                    border = BorderStroke(1.dp, ClayBorder.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Discount, contentDescription = null, tint = ParrotGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Have a Promo / Discount Coupon?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepNeem
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (appliedCoupon != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PistachioMist)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ParrotGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Coupon '$appliedCoupon' Applied!",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNeem
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
                                    colors = ButtonDefaults.buttonColors(containerColor = ParrotGreen),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Apply", color = Color.White)
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

                // 3. Indian Delivery Details & Address Form
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, ClayBorder.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delivery Address & Contact",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepNeem
                            )
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Terracotta)
                        }

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
                                label = { Text("Mobile (+91) *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                label = { Text("Email *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        OutlinedTextField(
                            value = flatHouseInput,
                            onValueChange = { flatHouseInput = it },
                            label = { Text("Flat / House No., Building / Apartment *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = streetInput,
                            onValueChange = { streetInput = it },
                            label = { Text("Street / Road / Sector / Colony *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = localityInput,
                                onValueChange = { localityInput = it },
                                label = { Text("Area / Locality") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = landmarkInput,
                                onValueChange = { landmarkInput = it },
                                label = { Text("Landmark (Optional)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cityInput,
                                onValueChange = { cityInput = it },
                                label = { Text("City *") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = pinCodeInput,
                                onValueChange = { pinCodeInput = it },
                                label = { Text("PIN Code *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // State / UT Selection Dropdown
                        ExposedDropdownMenuBox(
                            expanded = stateDropdownExpanded,
                            onExpandedChange = { stateDropdownExpanded = !stateDropdownExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = stateInput,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("State / UT *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = stateDropdownExpanded,
                                onDismissRequest = { stateDropdownExpanded = false }
                            ) {
                                IndianStatesAndUTs.forEach { stateName ->
                                    DropdownMenuItem(
                                        text = { Text(stateName) },
                                        onClick = {
                                            stateInput = stateName
                                            stateDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Delivery Method Selection
                        Text(
                            text = "Choose Nursery Delivery Method",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepNeem
                        )

                        // Standard Delivery
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (deliveryOption == DeliveryOption.STANDARD_DELIVERY) PistachioMist else Color.Transparent)
                                .clickable { deliveryOption = DeliveryOption.STANDARD_DELIVERY }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = deliveryOption == DeliveryOption.STANDARD_DELIVERY,
                                onClick = { deliveryOption = DeliveryOption.STANDARD_DELIVERY },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("Standard Plant-Safe Delivery (₹99)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                                Text("Delivered in 3-5 business days (FREE on orders over ₹999)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Express Delivery
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (deliveryOption == DeliveryOption.EXPRESS_DELIVERY) PistachioMist else Color.Transparent)
                                .clickable { deliveryOption = DeliveryOption.EXPRESS_DELIVERY }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = deliveryOption == DeliveryOption.EXPRESS_DELIVERY,
                                onClick = { deliveryOption = DeliveryOption.EXPRESS_DELIVERY },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("Express Priority Delivery (₹199)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                                Text("Delivered in 1-2 business days with special climate cushioning", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Store Pickup
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (deliveryOption == DeliveryOption.STORE_PICKUP) PistachioMist else Color.Transparent)
                                .clickable { deliveryOption = DeliveryOption.STORE_PICKUP }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = deliveryOption == DeliveryOption.STORE_PICKUP,
                                onClick = { deliveryOption = DeliveryOption.STORE_PICKUP },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("Terrarium Experience Nursery Pickup (FREE)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                                Text("Ready in 2 hours at Terrarium Greenhouse Studios", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Payment Method Selection
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepNeem
                        )

                        // UPI
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (paymentMethod == PaymentMethod.UPI) PistachioMist else Color.Transparent)
                                .clickable { paymentMethod = PaymentMethod.UPI }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.UPI,
                                onClick = { paymentMethod = PaymentMethod.UPI },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Icon(Icons.Default.QrCode, contentDescription = null, tint = ParrotGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("UPI (Google Pay, PhonePe, Paytm, BHIM)", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                        }

                        // Cash on Delivery
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (paymentMethod == PaymentMethod.CASH_ON_DELIVERY) PistachioMist else Color.Transparent)
                                .clickable { paymentMethod = PaymentMethod.CASH_ON_DELIVERY }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.CASH_ON_DELIVERY,
                                onClick = { paymentMethod = PaymentMethod.CASH_ON_DELIVERY },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Terracotta, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cash on Delivery (Pay upon safe doorstep arrival)", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                        }

                        // Card
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (paymentMethod == PaymentMethod.CARD) PistachioMist else Color.Transparent)
                                .clickable { paymentMethod = PaymentMethod.CARD }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.CARD,
                                onClick = { paymentMethod = PaymentMethod.CARD },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = ParrotGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Debit / Credit Card (Visa, RuPay, MasterCard)", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                        }

                        // Net Banking
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (paymentMethod == PaymentMethod.NET_BANKING) PistachioMist else Color.Transparent)
                                .clickable { paymentMethod = PaymentMethod.NET_BANKING }
                                .padding(6.dp)
                        ) {
                            RadioButton(
                                selected = paymentMethod == PaymentMethod.NET_BANKING,
                                onClick = { paymentMethod = PaymentMethod.NET_BANKING },
                                colors = RadioButtonDefaults.colors(selectedColor = ParrotGreen)
                            )
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = DeepNeem, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Net Banking (SBI, HDFC, ICICI, Axis)", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Order Summary & Totals
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, ClayBorder.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Price Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepNeem
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                            Text(formatRupees(subtotal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = DeepNeem)
                        }

                        if (discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Coupon Discount ($appliedCoupon)", style = MaterialTheme.typography.bodyMedium, color = ParrotGreen)
                                Text("-${formatRupees(discountAmount)}", style = MaterialTheme.typography.bodyMedium, color = ParrotGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Plant-Safe Delivery Fee", style = MaterialTheme.typography.bodyMedium, color = DeepNeem)
                            Text(
                                if (deliveryFee == 0.0) "FREE" else formatRupees(deliveryFee),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (deliveryFee == 0.0) ParrotGreen else DeepNeem
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = ClayBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DeepNeem
                            )
                            Text(
                                text = formatRupees(grandTotal),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = ParrotGreen
                            )
                        }

                        // Demo Notice Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(PistachioMist)
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = DeepNeem, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Safe & Secure Demo Checkout — No real charge incurred.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DeepNeem
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
                                if (nameInput.isBlank() || phoneInput.isBlank() || emailInput.isBlank() || flatHouseInput.isBlank() || streetInput.isBlank() || cityInput.isBlank() || pinCodeInput.isBlank()) {
                                    validationError = "Please fill in all mandatory fields marked with *."
                                } else {
                                    viewModel.updateUserProfile(
                                        name = nameInput,
                                        email = emailInput,
                                        phone = phoneInput,
                                        flatHouse = flatHouseInput,
                                        street = streetInput,
                                        locality = localityInput,
                                        landmark = landmarkInput,
                                        city = cityInput,
                                        state = stateInput,
                                        pinCode = pinCodeInput
                                    )
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
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("cart_place_order_btn")
                        ) {
                            Text(
                                text = "Place Order (${formatRupees(grandTotal)})",
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
