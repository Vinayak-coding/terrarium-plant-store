package com.example.ui.screens.order

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.TerrariumViewModel

val orderStatuses = listOf(
    OrderStatus.ORDER_PLACED,
    OrderStatus.ORDER_CONFIRMED,
    OrderStatus.PACKED,
    OrderStatus.DISPATCHED,
    OrderStatus.OUT_FOR_DELIVERY,
    OrderStatus.DELIVERED
)

@Composable
fun OrderTrackingScreen(
    initialOrderId: String? = null,
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.allOrders.collectAsState()
    var selectedOrder by remember(initialOrderId, orders) {
        mutableStateOf(
            if (initialOrderId != null) orders.find { it.orderId == initialOrderId }
            else orders.firstOrNull()
        )
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
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
                    text = "Track Orders & History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
            }

            if (orders.isEmpty()) {
                EmptyStateView(
                    title = "No orders found",
                    message = "Place your first plant order to track it live here.",
                    icon = Icons.Default.LocalShipping,
                    actionButtonText = "Start Shopping",
                    onActionClick = onExploreClick
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Active Selected Order Details & Visual Timeline
                    selectedOrder?.let { order ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("order_tracking_card_${order.orderId}")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Order ID & Status Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Order #${order.orderId}",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenDark
                                        )
                                        Text(
                                            text = "Expected by: ${order.expectedDeliveryDate}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ForestGreenPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MintLight)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = order.status.displayName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenDark
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Interactive Step Timeline
                                Text(
                                    text = "Fulfillment Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenDark
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                val currentStatusIndex = orderStatuses.indexOf(order.status).let { if (it == -1) 0 else it }

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    orderStatuses.forEachIndexed { index, status ->
                                        val isCompleted = index <= currentStatusIndex
                                        val isCurrent = index == currentStatusIndex

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when {
                                                            isCurrent -> TerracottaAccent
                                                            isCompleted -> ForestGreenPrimary
                                                            else -> Color.LightGray.copy(alpha = 0.5f)
                                                        }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isCompleted) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column {
                                                Text(
                                                    text = status.displayName,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isCompleted) ForestGreenDark else Color.Gray
                                                )
                                                if (isCurrent) {
                                                    Text(
                                                        text = "Current Status",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = TerracottaAccent,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                                // Items & Summary
                                Text(
                                    text = "Order Breakdown",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = order.itemsSummary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Delivery Address", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text("${order.address}, ${order.city}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Total Paid", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ForestGreenDark)
                                    Text("$${String.format("%.2f", order.totalAmount)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                }
                            }
                        }
                    }

                    // Order History List (if more than 1)
                    if (orders.size > 1) {
                        Text(
                            text = "All Recent Orders",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        orders.forEach { orderItem ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedOrder = orderItem }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("#${orderItem.orderId}", fontWeight = FontWeight.Bold, color = ForestGreenDark)
                                        Text(orderItem.itemsSummary, maxLines = 1, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                    Text("$${String.format("%.2f", orderItem.totalAmount)}", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
