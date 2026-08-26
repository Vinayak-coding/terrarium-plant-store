package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SunAmber
import com.example.ui.theme.TerracottaAccent
import com.example.ui.theme.TerracottaLight

@Composable
fun RatingStars(
    rating: Float,
    reviewCount: Int? = null,
    starSize: Dp = 14.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.5f

        for (i in 1..5) {
            when {
                i <= fullStars -> {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = SunAmber,
                        modifier = Modifier.size(starSize)
                    )
                }
                i == fullStars + 1 && hasHalf -> {
                    Icon(
                        imageVector = Icons.Default.StarHalf,
                        contentDescription = null,
                        tint = SunAmber,
                        modifier = Modifier.size(starSize)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(starSize)
                    )
                }
            }
        }
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = ForestGreenDark,
            modifier = Modifier.padding(start = 4.dp)
        )
        if (reviewCount != null) {
            Text(
                text = "($reviewCount)",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StockBadge(
    stock: Int,
    modifier: Modifier = Modifier
) {
    val inStock = stock > 0
    val isLowStock = stock in 1..5

    val bgColor = when {
        !inStock -> AlertRed.copy(alpha = 0.12f)
        isLowStock -> SunAmber.copy(alpha = 0.15f)
        else -> MintLight
    }
    val textColor = when {
        !inStock -> AlertRed
        isLowStock -> Color(0xFFC67D00)
        else -> SuccessGreen
    }
    val text = when {
        !inStock -> "Out of Stock"
        isLowStock -> "Only $stock Left!"
        else -> "In Stock ($stock)"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}

@Composable
fun PlantTagChip(
    tag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SoftSageContainer)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = ForestGreenDark
        )
    }
}
