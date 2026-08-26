package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.MaintenanceLevel
import com.example.data.model.OrderStatus
import com.example.data.model.PlantSize
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.SunlightRequirement
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.RatingStars
import com.example.ui.components.StockBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SunAmber
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.TerrariumViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products by viewModel.allProducts.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val reviews by viewModel.allReviews.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Products (${products.size})", "Orders (${orders.size})", "Reviews (${reviews.size})")

    var showAddProductDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Product?>(null) }

    val totalRevenue = orders.sumOf { it.totalAmount }
    val lowStockCount = products.count { it.stock in 1..5 }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showAddProductDialog = true },
                    containerColor = TerracottaAccent,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("admin_add_product_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header with Switch to Customer Mode
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ForestGreenDark)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Terrarium Store Admin",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "Catalog, Inventory & Order Fulfillment",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = { viewModel.toggleAdminMode() },
                    colors = ButtonDefaults.buttonColors(containerColor = SoftSageContainer),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreenDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Customer", style = MaterialTheme.typography.labelSmall, color = ForestGreenDark, fontWeight = FontWeight.Bold)
                }
            }

            // Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ForestGreenDark,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = ForestGreenPrimary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // KPI Overview
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminKpiCard(
                                title = "Demo Revenue",
                                value = "$${String.format("%.2f", totalRevenue)}",
                                icon = Icons.Default.AttachMoney,
                                color = ForestGreenPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            AdminKpiCard(
                                title = "Total Orders",
                                value = "${orders.size}",
                                icon = Icons.Default.LocalShipping,
                                color = TerracottaAccent,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminKpiCard(
                                title = "Catalog Items",
                                value = "${products.size}",
                                icon = Icons.Default.Inventory,
                                color = ForestGreenDark,
                                modifier = Modifier.weight(1f)
                            )
                            AdminKpiCard(
                                title = "Low Stock Alerts",
                                value = "$lowStockCount Items",
                                icon = Icons.Default.Warning,
                                color = SunAmber,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Recent Orders preview
                        Text(
                            text = "Recent Store Orders",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        if (orders.isEmpty()) {
                            Text("No store orders placed yet.", color = Color.Gray)
                        } else {
                            orders.take(3).forEach { order ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("#${order.orderId} • ${order.customerName}", fontWeight = FontWeight.Bold)
                                            Text(order.status.displayName, style = MaterialTheme.typography.bodySmall, color = ForestGreenPrimary)
                                        }
                                        Text("$${String.format("%.2f", order.totalAmount)}", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Products Management List
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products, key = { it.id }) { product ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        BotanicalIllustration(imageIndex = product.imageIndex, modifier = Modifier.fillMaxSize())
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                                        Text("${product.category.displayName} • $${product.price} (Stock: ${product.stock})", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        StockBadge(stock = product.stock)
                                    }

                                    Row {
                                        IconButton(onClick = { productToEdit = product }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ForestGreenPrimary)
                                        }
                                        IconButton(onClick = { viewModel.deleteProduct(product) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AlertRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Orders Management
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(orders, key = { it.orderId }) { order ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("#${order.orderId}", fontWeight = FontWeight.Bold, color = ForestGreenDark)
                                            Text("Customer: ${order.customerName} (${order.phone})", style = MaterialTheme.typography.bodySmall)
                                        }
                                        Text("$${String.format("%.2f", order.totalAmount)}", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Items: ${order.itemsSummary}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Current: ${order.status.displayName}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)

                                        // Status Change Dropdown Menu
                                        var showMenu by remember { mutableStateOf(false) }
                                        Box {
                                            OutlinedButton(
                                                onClick = { showMenu = true },
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("Update Status", style = MaterialTheme.typography.labelSmall)
                                            }

                                            DropdownMenu(
                                                expanded = showMenu,
                                                onDismissRequest = { showMenu = false }
                                            ) {
                                                OrderStatus.values().forEach { status ->
                                                    DropdownMenuItem(
                                                        text = { Text(status.displayName) },
                                                        onClick = {
                                                            viewModel.updateOrderStatus(order.orderId, status.name)
                                                            showMenu = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Reviews Moderation
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(reviews, key = { it.id }) { review ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${review.customerName} (${review.rating} ★)", fontWeight = FontWeight.Bold)
                                        Text(review.comment, style = MaterialTheme.typography.bodySmall)
                                    }

                                    IconButton(onClick = { viewModel.updateReviewApproval(review.id, !review.isApproved) }) {
                                        Icon(
                                            imageVector = if (review.isApproved) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (review.isApproved) SuccessGreen else AlertRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Product Dialog
    if (showAddProductDialog || productToEdit != null) {
        val isEditing = productToEdit != null
        val initialProduct = productToEdit ?: Product(
            id = System.currentTimeMillis(),
            name = "",
            category = ProductCategory.INDOOR_PLANTS,
            price = 20.0,
            discountPrice = null,
            rating = 4.8f,
            reviewCount = 1,
            stock = 15,
            shortDescription = "Fresh urban plant.",
            description = "High quality healthy botanical plant for urban homes.",
            tagLabel = "New Item",
            plantSize = PlantSize.MEDIUM,
            potSize = "6\" Ceramic Pot",
            sunlight = SunlightRequirement.MEDIUM_INDIRECT,
            wateringFrequency = "Once a week",
            maintenance = MaintenanceLevel.EASY,
            isIndoor = true,
            isOutdoor = false,
            isPetFriendly = true,
            benefits = "Air purifying",
            suitableSpaces = "Living Room, Bedroom, Office",
            isFeatured = true,
            isNewArrival = true,
            isBestSeller = false,
            isPlant = true,
            imageIndex = 1
        )

        var name by remember { mutableStateOf(initialProduct.name) }
        var price by remember { mutableStateOf("${initialProduct.price}") }
        var stock by remember { mutableStateOf("${initialProduct.stock}") }
        var shortDesc by remember { mutableStateOf(initialProduct.shortDescription) }

        AlertDialog(
            onDismissRequest = {
                showAddProductDialog = false
                productToEdit = null
            },
            title = {
                Text(if (isEditing) "Edit Product" else "Add New Product", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name *") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price ($) *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock Quantity *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = shortDesc, onValueChange = { shortDesc = it }, label = { Text("Short Description") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = price.toDoubleOrNull() ?: 20.0
                        val parsedStock = stock.toIntOrNull() ?: 10
                        val newOrUpdated = initialProduct.copy(
                            name = name,
                            price = parsedPrice,
                            stock = parsedStock,
                            shortDescription = shortDesc
                        )
                        if (isEditing) {
                            viewModel.updateProduct(newOrUpdated)
                        } else {
                            viewModel.addProduct(newOrUpdated)
                        }
                        showAddProductDialog = false
                        productToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text(if (isEditing) "Save Changes" else "Add to Inventory")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddProductDialog = false
                    productToEdit = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminKpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ForestGreenDark)
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
