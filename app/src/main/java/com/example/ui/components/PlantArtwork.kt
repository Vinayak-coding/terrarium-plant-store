package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.WaterBlue

@Composable
fun BotanicalIllustration(
    imageIndex: Int,
    modifier: Modifier = Modifier,
    categoryName: String = ""
) {
    val bgGradients = listOf(
        listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)), // Mint soft
        listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)), // Warm terracotta soft
        listOf(Color(0xFFE0F2F1), Color(0xFFB2DFDB)), // Teal misty
        listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8)), // Fresh foliage
        listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9)), // Soft lavender
        listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))  // Sunlight cream
    )
    val chosenGradient = bgGradients[(imageIndex + 1) % bgGradients.size]

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(chosenGradient)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Decorative background soft sun circle
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = minOf(w, h) * 0.38f,
                center = Offset(cx, cy * 0.9f)
            )

            when (imageIndex) {
                1 -> {
                    // Snake Plant: Tall upright variegated sword leaves & pot
                    // Terracotta pot
                    val potPath = Path().apply {
                        moveTo(cx - w * 0.22f, h * 0.68f)
                        lineTo(cx + w * 0.22f, h * 0.68f)
                        lineTo(cx + w * 0.17f, h * 0.90f)
                        lineTo(cx - w * 0.17f, h * 0.90f)
                        close()
                    }
                    drawPath(potPath, color = TerracottaAccent)
                    drawRect(
                        color = TerracottaDark,
                        topLeft = Offset(cx - w * 0.24f, h * 0.65f),
                        size = Size(w * 0.48f, h * 0.04f)
                    )
                    // Tall sword leaves
                    val leaf1 = Path().apply {
                        moveTo(cx, h * 0.66f)
                        cubicTo(cx - w * 0.15f, h * 0.40f, cx - w * 0.08f, h * 0.22f, cx - w * 0.05f, h * 0.12f)
                        cubicTo(cx - w * 0.02f, h * 0.25f, cx + w * 0.05f, h * 0.45f, cx, h * 0.66f)
                        close()
                    }
                    drawPath(leaf1, color = ForestGreenPrimary)
                    // Side leaves
                    val leaf2 = Path().apply {
                        moveTo(cx - w * 0.08f, h * 0.66f)
                        cubicTo(cx - w * 0.25f, h * 0.45f, cx - w * 0.20f, h * 0.28f, cx - w * 0.16f, h * 0.20f)
                        cubicTo(cx - w * 0.10f, h * 0.32f, cx - w * 0.02f, h * 0.50f, cx - w * 0.08f, h * 0.66f)
                        close()
                    }
                    drawPath(leaf2, color = SageGreen)
                    val leaf3 = Path().apply {
                        moveTo(cx + w * 0.08f, h * 0.66f)
                        cubicTo(cx + w * 0.25f, h * 0.45f, cx + w * 0.20f, h * 0.28f, cx + w * 0.16f, h * 0.20f)
                        cubicTo(cx + w * 0.10f, h * 0.32f, cx + w * 0.02f, h * 0.50f, cx + w * 0.08f, h * 0.66f)
                        close()
                    }
                    drawPath(leaf3, color = ForestGreenDark)
                }
                2 -> {
                    // Golden Pothos: Trailing heart leaves
                    // Hanging rope
                    drawLine(Color(0xFF8D6E63), Offset(cx, 0f), Offset(cx - w * 0.18f, h * 0.48f), strokeWidth = 3f)
                    drawLine(Color(0xFF8D6E63), Offset(cx, 0f), Offset(cx + w * 0.18f, h * 0.48f), strokeWidth = 3f)
                    // Bowl
                    drawArc(
                        color = Color(0xFF5D4037),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(cx - w * 0.22f, h * 0.40f),
                        size = Size(w * 0.44f, h * 0.22f)
                    )
                    // Cascading vines
                    drawCircle(color = ForestGreenPrimary, radius = w * 0.09f, center = Offset(cx - w * 0.18f, h * 0.58f))
                    drawCircle(color = SageGreen, radius = w * 0.08f, center = Offset(cx + w * 0.20f, h * 0.62f))
                    drawCircle(color = Color(0xFF81C784), radius = w * 0.07f, center = Offset(cx - w * 0.12f, h * 0.74f))
                    drawCircle(color = ForestGreenDark, radius = w * 0.08f, center = Offset(cx + w * 0.12f, h * 0.78f))
                    drawCircle(color = Color(0xFFA5D6A7), radius = w * 0.06f, center = Offset(cx - w * 0.05f, h * 0.86f))
                }
                3 -> {
                    // Peace Lily: Lush broad leaves & White Peace Lily spathe
                    val potPath = Path().apply {
                        moveTo(cx - w * 0.20f, h * 0.70f)
                        lineTo(cx + w * 0.20f, h * 0.70f)
                        lineTo(cx + w * 0.15f, h * 0.90f)
                        lineTo(cx - w * 0.15f, h * 0.90f)
                        close()
                    }
                    drawPath(potPath, color = Color(0xFF6D4C41))
                    // Broad foliage
                    drawCircle(color = ForestGreenDark, radius = w * 0.14f, center = Offset(cx - w * 0.14f, h * 0.58f))
                    drawCircle(color = ForestGreenPrimary, radius = w * 0.15f, center = Offset(cx + w * 0.14f, h * 0.56f))
                    drawCircle(color = SageGreen, radius = w * 0.12f, center = Offset(cx, h * 0.62f))
                    // White Peace Spathe & spadix
                    val spathe = Path().apply {
                        moveTo(cx, h * 0.48f)
                        cubicTo(cx - w * 0.12f, h * 0.35f, cx - w * 0.08f, h * 0.18f, cx, h * 0.12f)
                        cubicTo(cx + w * 0.08f, h * 0.18f, cx + w * 0.12f, h * 0.35f, cx, h * 0.48f)
                        close()
                    }
                    drawPath(spathe, color = Color.White)
                    drawLine(Color(0xFFFFD54F), Offset(cx, h * 0.42f), Offset(cx, h * 0.22f), strokeWidth = 8f)
                }
                5 -> {
                    // Aloe Vera: Serrated succulent rosettes
                    val pot = Path().apply {
                        moveTo(cx - w * 0.20f, h * 0.72f)
                        lineTo(cx + w * 0.20f, h * 0.72f)
                        lineTo(cx + w * 0.16f, h * 0.92f)
                        lineTo(cx - w * 0.16f, h * 0.92f)
                        close()
                    }
                    drawPath(pot, color = TerracottaAccent)
                    // Plump aloe leaves
                    val aloeL = Path().apply {
                        moveTo(cx, h * 0.70f)
                        cubicTo(cx - w * 0.30f, h * 0.55f, cx - w * 0.32f, h * 0.35f, cx - w * 0.28f, h * 0.24f)
                        cubicTo(cx - w * 0.18f, h * 0.38f, cx - w * 0.08f, h * 0.55f, cx, h * 0.70f)
                        close()
                    }
                    drawPath(aloeL, color = SageGreen)
                    val aloeR = Path().apply {
                        moveTo(cx, h * 0.70f)
                        cubicTo(cx + w * 0.30f, h * 0.55f, cx + w * 0.32f, h * 0.35f, cx + w * 0.28f, h * 0.24f)
                        cubicTo(cx + w * 0.18f, h * 0.38f, cx + w * 0.08f, h * 0.55f, cx, h * 0.70f)
                        close()
                    }
                    drawPath(aloeR, color = ForestGreenPrimary)
                    val aloeC = Path().apply {
                        moveTo(cx, h * 0.70f)
                        cubicTo(cx - w * 0.08f, h * 0.40f, cx - w * 0.04f, h * 0.22f, cx, h * 0.15f)
                        cubicTo(cx + w * 0.04f, h * 0.22f, cx + w * 0.08f, h * 0.40f, cx, h * 0.70f)
                        close()
                    }
                    drawPath(aloeC, color = Color(0xFF66BB6A))
                }
                9, 11 -> {
                    // Ceramic / Self-watering Pot
                    val pot = Path().apply {
                        moveTo(cx - w * 0.28f, h * 0.35f)
                        lineTo(cx + w * 0.28f, h * 0.35f)
                        lineTo(cx + w * 0.22f, h * 0.75f)
                        lineTo(cx - w * 0.22f, h * 0.75f)
                        close()
                    }
                    drawPath(pot, color = if (imageIndex == 9) SageGreen else Color(0xFF37474F))
                    // Saucer
                    drawRoundRect(
                        color = if (imageIndex == 9) TerracottaAccent else WaterBlue,
                        topLeft = Offset(cx - w * 0.30f, h * 0.75f),
                        size = Size(w * 0.60f, h * 0.08f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
                    )
                    // Sprout inside
                    drawCircle(color = Color(0xFF81C784), radius = w * 0.07f, center = Offset(cx - w * 0.06f, h * 0.28f))
                    drawCircle(color = ForestGreenPrimary, radius = w * 0.07f, center = Offset(cx + w * 0.06f, h * 0.26f))
                }
                12, 13 -> {
                    // Soil Mix / Cocopeat
                    drawRoundRect(
                        color = Color(0xFF5D4037),
                        topLeft = Offset(cx - w * 0.26f, h * 0.32f),
                        size = Size(w * 0.52f, h * 0.52f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
                    )
                    drawCircle(color = SageGreen, radius = w * 0.10f, center = Offset(cx, cy))
                }
                14 -> {
                    // Spray Bottle
                    drawRoundRect(
                        color = ForestGreenPrimary,
                        topLeft = Offset(cx - w * 0.16f, h * 0.42f),
                        size = Size(w * 0.32f, h * 0.44f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                    )
                    // Spray trigger
                    drawRect(color = TerracottaAccent, topLeft = Offset(cx - w * 0.12f, h * 0.30f), size = Size(w * 0.24f, h * 0.12f))
                    drawLine(TerracottaAccent, Offset(cx - w * 0.12f, h * 0.33f), Offset(cx - w * 0.28f, h * 0.36f), strokeWidth = 10f)
                }
                15 -> {
                    // Garden Tools
                    drawLine(Color(0xFF8D6E63), Offset(cx - w * 0.20f, h * 0.75f), Offset(cx + w * 0.20f, h * 0.25f), strokeWidth = 12f)
                    drawCircle(color = Color(0xFFFFB300), radius = w * 0.12f, center = Offset(cx + w * 0.20f, h * 0.25f))
                    drawLine(Color(0xFF8D6E63), Offset(cx + w * 0.20f, h * 0.75f), Offset(cx - w * 0.20f, h * 0.25f), strokeWidth = 12f)
                    drawCircle(color = TerracottaAccent, radius = w * 0.12f, center = Offset(cx - w * 0.20f, h * 0.25f))
                }
                else -> {
                    // General Lush Indoor Greenery / Fig / Succulent
                    val pot = Path().apply {
                        moveTo(cx - w * 0.22f, h * 0.68f)
                        lineTo(cx + w * 0.22f, h * 0.68f)
                        lineTo(cx + w * 0.16f, h * 0.90f)
                        lineTo(cx - w * 0.16f, h * 0.90f)
                        close()
                    }
                    drawPath(pot, color = TerracottaAccent)
                    drawCircle(color = ForestGreenPrimary, radius = w * 0.18f, center = Offset(cx, h * 0.45f))
                    drawCircle(color = SageGreen, radius = w * 0.14f, center = Offset(cx - w * 0.16f, h * 0.40f))
                    drawCircle(color = ForestGreenDark, radius = w * 0.14f, center = Offset(cx + w * 0.16f, h * 0.42f))
                    drawCircle(color = Color(0xFFA5D6A7), radius = w * 0.10f, center = Offset(cx, h * 0.30f))
                }
            }
        }
    }
}
