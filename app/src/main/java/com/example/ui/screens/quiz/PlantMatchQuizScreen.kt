package com.example.ui.screens.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizRecommendationResult
import com.example.ui.components.BotanicalIllustration
import com.example.ui.components.BundleKitCard
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SunAmber
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaDark
import com.example.ui.viewmodel.TerrariumViewModel

data class QuizQuestion(
    val key: String,
    val title: String,
    val subtitle: String,
    val options: List<QuizOption>
)

data class QuizOption(
    val label: String,
    val value: String,
    val description: String
)

val quizQuestions = listOf(
    QuizQuestion(
        key = "space",
        title = "Where will your new plant live?",
        subtitle = "Select your primary room or space",
        options = listOf(
            QuizOption("Living Room", "Living Room", "Open space, variable ambient light"),
            QuizOption("Bedroom", "Bedroom", "Nighttime oxygen & relaxing greenery"),
            QuizOption("Office / Desk", "Office Desk", "Workstation companion, compact footprint"),
            QuizOption("Balcony / Patio", "Balcony", "Outdoor fresh air & bright sun exposure"),
            QuizOption("Kitchen / Dining", "Kitchen", "Culinary herbs & sunny windowsill"),
            QuizOption("Small Apartment", "Small Spaces", "Space-saving trailing vines & shelf plants")
        )
    ),
    QuizQuestion(
        key = "sunlight",
        title = "How much natural sunlight does this spot receive?",
        subtitle = "Sunlight is the food of your plant",
        options = listOf(
            QuizOption("Low / Dim Light", "Low", "Far from windows, soft ambient light or shaded room"),
            QuizOption("Medium / Indirect Light", "Medium", "Gentle bright filtered light, near East/North window"),
            QuizOption("Bright Indirect Sun", "Bright", "Sun-filled room without harsh burning rays"),
            QuizOption("Direct Harsh Sunlight", "Direct", "South or West facing window with direct rays")
        )
    ),
    QuizQuestion(
        key = "careFrequency",
        title = "How often would you like to water or tend to it?",
        subtitle = "Be honest — we have plants for every routine!",
        options = listOf(
            QuizOption("Forgetful / Busy (Rarely)", "Rarely", "Once every 2-4 weeks, super drought tolerant"),
            QuizOption("Moderate Routine", "Moderate", "Once a week checking and watering"),
            QuizOption("Frequent Attention", "Frequently", "Daily misting, grooming and regular care")
        )
    ),
    QuizQuestion(
        key = "experienceLevel",
        title = "What is your plant parenting experience?",
        subtitle = "We will calibrate the maintenance difficulty",
        options = listOf(
            QuizOption("Beginner / First-Timer", "Beginner", "I want an indestructible, forgiving plant"),
            QuizOption("Casual Green Thumb", "Casual", "I've kept a few plants alive before"),
            QuizOption("Experienced Botanist", "Experienced", "Ready for exotic, statement varieties")
        )
    ),
    QuizQuestion(
        key = "plantSize",
        title = "What plant size are you looking for?",
        subtitle = "From desktop cuties to floor trees",
        options = listOf(
            QuizOption("Small / Desktop (4\" - 6\")", "Small", "Perfect for tables, shelves and windowsills"),
            QuizOption("Medium Foliage (6\" - 8\")", "Medium", "Great for coffee tables and plant stands"),
            QuizOption("Large Statement (8\"+)", "Large", "Floor focal point for living rooms")
        )
    ),
    QuizQuestion(
        key = "purpose",
        title = "What is your main goal for this plant?",
        subtitle = "Choose your top botanical benefit",
        options = listOf(
            QuizOption("Clean & Purify Air", "Air", "NASA rated to remove indoor toxins & toxins"),
            QuizOption("100% Pet Friendly", "Pet", "Safe if curious cats or dogs take a nibble"),
            QuizOption("Fresh Culinary Herbs", "Herb", "Harvest delicious leaves for cooking"),
            QuizOption("Aesthetic Decor & Calm", "Decor", "Lush visual beauty and stress relief")
        )
    ),
    QuizQuestion(
        key = "budget",
        title = "What is your preferred budget?",
        subtitle = "Terrarium offers premium plants for every budget",
        options = listOf(
            QuizOption("Under $25", "Budget", "Affordable starter plants & pots"),
            QuizOption("$25 - $45", "Standard", "Complete potted favorites"),
            QuizOption("$45+", "Premium", "Large specimens & complete kit bundles")
        )
    )
)

@Composable
fun PlantMatchQuizScreen(
    viewModel: TerrariumViewModel,
    onNavigateToProductDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val quizStep by viewModel.quizStep.collectAsState()
    val preferences by viewModel.quizPreferences.collectAsState()
    val recommendations by viewModel.quizRecommendations.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (quizStep < 7) {
            // Interactive Questions Flow (Steps 0 to 6)
            val currentQuestion = quizQuestions[quizStep]
            val currentAnswer = when (currentQuestion.key) {
                "space" -> preferences.space
                "sunlight" -> preferences.sunlight
                "careFrequency" -> preferences.careFrequency
                "experienceLevel" -> preferences.experienceLevel
                "plantSize" -> preferences.plantSize
                "purpose" -> preferences.purpose
                "budget" -> preferences.budget
                else -> ""
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header & Progress Indicator
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (quizStep > 0) {
                            IconButton(onClick = { viewModel.previousQuizStep() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }

                        Text(
                            text = "Question ${quizStep + 1} of 7",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )

                        IconButton(onClick = { viewModel.resetQuiz() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = ForestGreenPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (quizStep + 1) / 7f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ForestGreenPrimary,
                        trackColor = SoftSageContainer
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = currentQuestion.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentQuestion.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Option Cards
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        currentQuestion.options.forEach { option ->
                            val isSelected = currentAnswer == option.value
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MintLight else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, ForestGreenPrimary) else null,
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setQuizAnswer(currentQuestion.key, option.value)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) ForestGreenPrimary else Color.LightGray.copy(alpha = 0.5f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = option.label,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenDark
                                        )
                                        Text(
                                            text = option.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next / Submit Button
                Button(
                    onClick = { viewModel.nextQuizStep() },
                    enabled = currentAnswer.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (quizStep == 6) "Show My Matches" else "Next Question",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        } else {
            // Results View (quizStep == 7)
            QuizResultsView(
                recommendations = recommendations,
                onRetake = { viewModel.resetQuiz() },
                onNavigateToProduct = onNavigateToProductDetail,
                onAddToCart = { viewModel.addToCart(it.id, 1) },
                onAddBundleToCart = { viewModel.addBundleToCart(it) }
            )
        }
    }
}

@Composable
fun QuizResultsView(
    recommendations: List<QuizRecommendationResult>,
    onRetake: () -> Unit,
    onNavigateToProduct: (Long) -> Unit,
    onAddToCart: (com.example.data.model.Product) -> Unit,
    onAddBundleToCart: (com.example.data.model.BundleKit) -> Unit
) {
    val topMatch = recommendations.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Success Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(ForestGreenDark, ForestGreenPrimary)
                    )
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(TerracottaAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We Found Your Perfect Match!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Based on your lighting, space and schedule, here is the botanical companion best suited for your home.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MintLight,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Top Recommendation Highlight Card
        topMatch?.let { match ->
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToProduct(match.product.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Match score pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ForestGreenPrimary)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = SunAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${match.matchPercentage}% Compatibility",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = "$${String.format("%.2f", match.product.discountPrice ?: match.product.price)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        BotanicalIllustration(
                            imageIndex = match.product.imageIndex,
                            categoryName = match.product.category.displayName,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = match.product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Explainable reasoning box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MintLight)
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Why: ${match.matchReason}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onAddToCart(match.product) },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Plant")
                        }

                        OutlinedButton(
                            onClick = { onNavigateToProduct(match.product.id) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("View Details", color = ForestGreenDark)
                        }
                    }
                }
            }

            // Recommended Bundle Kit Companion
            match.recommendedBundle?.let { bundle ->
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Recommended Kit Pairing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                BundleKitCard(
                    bundle = bundle,
                    onAddKitToCart = { onAddBundleToCart(bundle) }
                )
            }
        }

        // Other Runner-Up Matches
        if (recommendations.size > 1) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Other Great Matches for You",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            recommendations.drop(1).take(3).forEach { runnerUp ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { onNavigateToProduct(runnerUp.product.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            BotanicalIllustration(
                                imageIndex = runnerUp.product.imageIndex,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = runnerUp.product.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenDark
                                )
                                Text(
                                    text = "${runnerUp.matchPercentage}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            }
                            Text(
                                text = runnerUp.matchReason,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Retake Quiz Button
        Button(
            onClick = onRetake,
            colors = ButtonDefaults.buttonColors(containerColor = TerracottaAccent),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("quiz_retake_button")
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retake Quiz", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
