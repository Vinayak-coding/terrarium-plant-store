package com.example.ui.screens.space

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SpaceCategory
import com.example.ui.components.EmptyStateView
import com.example.ui.components.ProductGridCard
import com.example.ui.components.getSpaceIcon
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.TerrariumViewModel

fun getSpaceAdvice(space: SpaceCategory): String = when (space) {
    SpaceCategory.BEDROOM -> "Choose NASA air-purifying varieties like Snake Plants that release fresh oxygen at night and thrive in gentle ambient lighting."
    SpaceCategory.LIVING_ROOM -> "Ideal for statement architectural plants like Fiddle Leaf Fig and cascading Golden Pothos that enjoy medium bright indirect light."
    SpaceCategory.BALCONY -> "Great for sun-loving plants, edible herb kits and hardy succulents that love fresh outdoor air circulation and morning direct sunlight."
    SpaceCategory.OFFICE -> "Compact, drought-tolerant plants like ZZ Plants, Succulents, and self-watering pots that maintain a stress-free green work environment."
    SpaceCategory.KITCHEN -> "Thrives with culinary herbs like Italian Basil and sunny windowsill succulents that benefit from cooking humidity."
    SpaceCategory.BATHROOM -> "Humidity-loving plants like Peace Lilies and Pothos that thrive in warm shower mist and diffused indirect light."
    SpaceCategory.SMALL_SPACES -> "Space-efficient hanging planters, macrame holders, and compact desk planters that maximize vertical room."
    SpaceCategory.OUTDOOR_GARDEN -> "Durable outdoor soil mixes, organic neem sprays, and robust planters designed to withstand seasonal weather."
}

@Composable
fun ShopBySpaceScreen(
    initialSpace: SpaceCategory = SpaceCategory.BEDROOM,
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    onNavigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSpace by remember { mutableStateOf(initialSpace) }
    val allProducts by viewModel.allProducts.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val wishlistIds = wishlistItems.map { it.productId }.toSet()

    val spaceProducts = allProducts.filter { product ->
        product.suitableSpaces.contains(selectedSpace.displayName, ignoreCase = true) ||
        (selectedSpace == SpaceCategory.BEDROOM && (product.id == 1L || product.id == 3L || product.id == 4L || product.id == 9L)) ||
        (selectedSpace == SpaceCategory.BALCONY && (product.id == 2L || product.id == 5L || product.id == 8L || product.id == 10L || product.id == 13L || product.id == 15L)) ||
        (selectedSpace == SpaceCategory.OFFICE && (product.id == 1L || product.id == 6L || product.id == 7L || product.id == 11L || product.id == 15L)) ||
        (selectedSpace == SpaceCategory.KITCHEN && (product.id == 2L || product.id == 5L || product.id == 8L || product.id == 9L || product.id == 12L))
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
                    text = "Shop by Space",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
            }

            // Horizontal Space Selector Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpaceCategory.values().forEach { space ->
                    val isSelected = selectedSpace == space
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ForestGreenPrimary else SoftSageContainer)
                            .clickable { selectedSpace = space }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = getSpaceIcon(space),
                                contentDescription = null,
                                tint = if (isSelected) Color.White else ForestGreenDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = space.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else ForestGreenDark
                            )
                        }
                    }
                }
            }

            // Room Environmental Advice Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MintLight)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Best Botanical Setup for ${selectedSpace.displayName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getSpaceAdvice(selectedSpace),
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreenDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Products Grid for this Space
            if (spaceProducts.isEmpty()) {
                EmptyStateView(
                    title = "No specific items for ${selectedSpace.displayName}",
                    message = "Check out our all-products catalog for general botanical options."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(spaceProducts, key = { it.id }) { product ->
                        ProductGridCard(
                            product = product,
                            isInWishlist = wishlistIds.contains(product.id),
                            onProductClick = { onNavigateToProductDetail(product.id) },
                            onWishlistToggle = { viewModel.toggleWishlist(product.id) },
                            onAddToCart = { viewModel.addToCart(product.id, 1) }
                        )
                    }
                }
            }
        }
    }
}
