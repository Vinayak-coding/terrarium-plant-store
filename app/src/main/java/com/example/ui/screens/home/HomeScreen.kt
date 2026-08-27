package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BundleKit
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.SpaceCategory
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.BundleKitCard
import com.example.ui.components.ProductGridCard
import com.example.ui.components.RatingStars
import com.example.ui.components.SpaceCarouselCard
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SunAmber
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaLight
import com.example.ui.viewmodel.TerrariumViewModel

@Composable
fun HomeScreen(
    viewModel: TerrariumViewModel,
    onNavigateToCatalogue: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToSpace: (SpaceCategory) -> Unit,
    onNavigateToProductDetail: (Long) -> Unit,
    onNavigateToBundles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredProducts by viewModel.featuredProducts.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val bestSellers by viewModel.bestSellers.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val wishlistIds = wishlistItems.map { it.productId }.toSet()
    val bundleKits = viewModel.bundleKits

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // 1. Hero Banner: Plant Match Quiz Promotion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(ForestGreenDark, ForestGreenPrimary)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(TerracottaAccent)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI-Powered Plant Match Quiz",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Find Your Ideal Plant in 60 Seconds",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Answer 7 quick questions about your room lighting, space & schedule to get tailor-made botanical matches.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MintLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onNavigateToQuiz,
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("home_take_quiz_cta")
                    ) {
                        Text("Find My Plant", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    OutlinedButton(
                        onClick = onNavigateToCatalogue,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("home_shop_now_cta")
                    ) {
                        Text("Shop Now", color = Color.White)
                    }
                }
            }
        }

        // 2. Promotional Discount & Store Pickup Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(TerracottaLight, Color(0xFFFFF3E0))
                        )
                    )
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(TerracottaAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Monsoon & Festive Green Sale",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaDark
                            )
                            Text(
                                text = "Use coupon GROW20 for 20% off all plants & kits",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForestGreenDark
                            )
                        }
                    }
                }
            }

            // Thane Store Partner & Pickup Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SoftSageContainer)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = ForestGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Store Pickup – Thane",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "Pickup from our Thane store or enjoy safe delivery across Maharashtra",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForestGreenPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Shop by Space Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shop by Space",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                TextButton(onClick = { onNavigateToSpace(SpaceCategory.BEDROOM) }) {
                    Text("View All", color = ForestGreenPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(SpaceCategory.values()) { space ->
                    SpaceCarouselCard(
                        space = space,
                        onClick = { onNavigateToSpace(space) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Shop by Plant Type (Quick Filter Pills)
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = "Shop by Plant Type",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            val plantCategories = listOf(
                ProductCategory.INDOOR_PLANTS,
                ProductCategory.FLOWERING_PLANTS,
                ProductCategory.SUCCULENTS,
                ProductCategory.HERBS,
                ProductCategory.LOW_LIGHT,
                ProductCategory.AIR_PURIFYING
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                plantCategories.forEach { category ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftSageContainer)
                            .clickable {
                                viewModel.setCategoryFilter(category)
                                onNavigateToCatalogue()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestGreenDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Featured Plants Carousel
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Plants",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                TextButton(onClick = onNavigateToCatalogue) {
                    Text("See More", color = ForestGreenPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(featuredProducts) { product ->
                    Box(modifier = Modifier.width(190.dp)) {
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

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Complete Plant-Care Kits Banner & Preview
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Complete Plant-Care Kits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )
                    Text(
                        text = "All-in-one curated kits with plant, pot & soil",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(onClick = onNavigateToBundles) {
                    Text("View Kits", color = ForestGreenPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            bundleKits.firstOrNull()?.let { bundle ->
                BundleKitCard(
                    bundle = bundle,
                    onAddKitToCart = { viewModel.addBundleToCart(bundle) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 7. Best Sellers & New Arrivals Row
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Best-Selling Plants",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                TextButton(onClick = onNavigateToCatalogue) {
                    Text("Browse All", color = ForestGreenPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(bestSellers) { product ->
                    Box(modifier = Modifier.width(190.dp)) {
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

        Spacer(modifier = Modifier.height(24.dp))

        // 8. Quick Gardening Tips Section
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Urban Gardening Tip of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The Finger Test: Always insert your finger 2 inches into the soil. If it feels cool and damp, delay watering by 2 days. More houseplants perish from overwatering than drought!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = onNavigateToGuides,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Read Full Care Guides", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 9. Customer Testimonial & Community
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = "Loved by Urban Plant Parents",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MintLight.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    RatingStars(rating = 5.0f, starSize = 14.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"The Plant Match Quiz matched me with a Snake Plant and ZZ Plant for my dimly-lit apartment. 6 months later, both are thriving and my air feels so fresh!\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ForestGreenDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "— Verified Customer, Thane",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                }
            }
        }
    }
}
