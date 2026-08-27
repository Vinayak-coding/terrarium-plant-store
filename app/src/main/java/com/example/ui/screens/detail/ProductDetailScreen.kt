package com.example.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.formatRupees
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.RatingStars
import com.example.ui.components.StockBadge
import com.example.ui.theme.ClayBorder
import com.example.ui.theme.DeepNeem
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.ParrotGreen
import com.example.ui.theme.PistachioMist
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SunAmber
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaLight
import com.example.ui.viewmodel.TerrariumViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    productId: Long,
    viewModel: TerrariumViewModel,
    onBackClick: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToGuides: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.find { it.id == productId }
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val isWishlisted = wishlistItems.any { it.productId == productId }
    val reviews by viewModel.getProductReviews(productId).collectAsState(initial = emptyList<Review>())

    var quantity by remember { mutableIntStateOf(1) }
    var showReviewDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (product == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // Persistent Bottom Sticky CTA Bar
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftSageContainer)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = { if (quantity < product.stock) quantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                        }
                    }

                    // Add to Cart button
                    Button(
                        onClick = {
                            viewModel.addToCart(product.id, quantity)
                            scope.launch {
                                snackbarHostState.showSnackbar("Added $quantity × ${product.name} to Cart!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("product_detail_add_to_cart_btn")
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart", fontWeight = FontWeight.Bold)
                    }

                    // Buy Now direct checkout button
                    Button(
                        onClick = {
                            viewModel.addToCart(product.id, quantity)
                            onNavigateToCheckout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaAccent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("product_detail_buy_now_btn")
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Buy Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image & Back / Wishlist floating bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                BotanicalIllustration(
                    imageIndex = product.imageIndex,
                    categoryName = product.category.displayName,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Navigation Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .testTag("detail_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ForestGreenDark)
                    }

                    IconButton(
                        onClick = { viewModel.toggleWishlist(product.id) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .testTag("detail_wishlist_button")
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) TerracottaAccent else ForestGreenDark
                        )
                    }
                }
            }

            // Main Product Details Info Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category & Stock Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.category.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        color = ForestGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    StockBadge(stock = product.stock)
                }

                // Name & Tagline
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )

                // Rating & Reviews row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingStars(
                        rating = product.rating,
                        reviewCount = product.reviewCount,
                        starSize = 18.dp
                    )
                    if (product.isPetFriendly) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MintLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("100% Pet Safe", style = MaterialTheme.typography.labelSmall, color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Price Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val displayPrice = product.discountPrice ?: product.price
                    Text(
                        text = formatRupees(displayPrice),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = ParrotGreen
                    )
                    if (product.discountPrice != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatRupees(product.price),
                            style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.LineThrough),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Terracotta)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("SAVE ${formatRupees(product.price - product.discountPrice!!)}", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider()

                // Care Metrics Grid Cards (Sunlight, Watering, Care Level, Size)
                Text(
                    text = "Care Requirements & Specifications",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CareMetricItem(
                        icon = Icons.Default.WbSunny,
                        title = "Sunlight",
                        value = product.sunlight.displayName,
                        modifier = Modifier.weight(1f)
                    )
                    CareMetricItem(
                        icon = Icons.Default.Opacity,
                        title = "Watering",
                        value = product.wateringFrequency,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CareMetricItem(
                        icon = Icons.Default.Spa,
                        title = "Maintenance",
                        value = product.maintenance.displayName,
                        modifier = Modifier.weight(1f)
                    )
                    CareMetricItem(
                        icon = Icons.Default.Straighten,
                        title = "Pot Size",
                        value = product.potSize,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Description & Benefits
                Text(
                    text = "About this Plant",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                if (product.benefits.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MintLight.copy(alpha = 0.5f))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Key Botanical Benefits",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.benefits,
                                style = MaterialTheme.typography.bodySmall,
                                color = ForestGreenDark
                            )
                        }
                    }
                }

                // Care Instructions & Guide link
                if (!product.careInstructions.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftSageContainer)
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Care Instructions",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = product.careInstructions,
                                style = MaterialTheme.typography.bodySmall,
                                color = ForestGreenDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = onNavigateToGuides,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("View Full Care Guide", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Customer Reviews Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer Reviews (${reviews.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                    TextButton(onClick = { showReviewDialog = true }) {
                        Text("Write Review", color = TerracottaAccent, fontWeight = FontWeight.Bold)
                    }
                }

                if (reviews.isEmpty()) {
                    Text(
                        text = "No reviews yet for this product. Be the first to share your experience!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    reviews.forEach { review ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = review.customerName,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenDark
                                    )
                                    Text(
                                        text = review.dateText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                RatingStars(rating = review.rating.toFloat(), starSize = 12.dp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = review.comment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Write a Review Dialog
    if (showReviewDialog) {
        var reviewerName by remember { mutableStateOf("") }
        var reviewRating by remember { mutableIntStateOf(5) }
        var reviewComment by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = {
                Text("Write a Review", style = MaterialTheme.typography.headlineSmall, color = ForestGreenDark)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("How was your plant parent experience?", style = MaterialTheme.typography.bodySmall)
                    
                    // Star Rating selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            IconButton(onClick = { reviewRating = i }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = if (i <= reviewRating) Icons.Default.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "$i Stars",
                                    tint = SunAmber
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Your Review / Plant Setup feedback") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewComment.isNotBlank()) {
                            viewModel.addReview(
                                productId = product.id,
                                author = reviewerName,
                                rating = reviewRating,
                                comment = reviewComment,
                                setupPhoto = null
                            )
                            showReviewDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Thank you for your review!")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Submit Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CareMetricItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SoftSageContainer),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ForestGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark,
                    maxLines = 1
                )
            }
        }
    }
}
