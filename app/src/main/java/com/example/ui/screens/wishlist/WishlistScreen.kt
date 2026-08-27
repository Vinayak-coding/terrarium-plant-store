package com.example.ui.screens.wishlist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.formatRupees
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StockBadge
import com.example.ui.theme.ClayBorder
import com.example.ui.theme.DeepNeem
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.ParrotGreen
import com.example.ui.theme.PistachioMist
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.TerrariumViewModel

@Composable
fun WishlistScreen(
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    onNavigateToProductDetail: (Long) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    val wishlistedProducts = wishlistItems.mapNotNull { item ->
        allProducts.find { it.id == item.productId }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "My Plant Wishlist (${wishlistedProducts.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
            }

            if (wishlistedProducts.isEmpty()) {
                EmptyStateView(
                    title = "Your wishlist is empty",
                    message = "Tap the heart icon on any plant or pot to save it for later.",
                    icon = Icons.Default.Favorite,
                    actionButtonText = "Discover Plants",
                    onActionClick = onExploreClick
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(wishlistedProducts, key = { it.id }) { product ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("wishlist_item_${product.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
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
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNeem,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = formatRupees(product.discountPrice ?: product.price),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ParrotGreen
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    StockBadge(stock = product.stock)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray)
                                    }
                                    Button(
                                        onClick = {
                                            viewModel.addToCart(product.id, 1)
                                            viewModel.toggleWishlist(product.id)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ParrotGreen),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Move", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
