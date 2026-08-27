package com.example.ui.screens.dashboard

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.SoftSageContainer
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.TerrariumViewModel

@Composable
fun CustomerDashboardScreen(
    viewModel: TerrariumViewModel,
    onNavigateToOrders: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()
    val reminders by viewModel.allReminders.collectAsState()
    val quizPreferences by viewModel.quizPreferences.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Hero Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SoftSageContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = user.membershipTier,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaAccent
                        )
                    }

                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = ForestGreenPrimary)
                    }
                }
            }

            // Quick Metric Counters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardMetricCard(
                    title = "Orders",
                    value = "${orders.size}",
                    icon = Icons.Default.LocalShipping,
                    onClick = onNavigateToOrders,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricCard(
                    title = "Wishlist",
                    value = "${wishlist.size}",
                    icon = Icons.Default.Favorite,
                    onClick = onNavigateToWishlist,
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricCard(
                    title = "Reminders",
                    value = "${reminders.size}",
                    icon = Icons.Default.Notifications,
                    onClick = onNavigateToReminders,
                    modifier = Modifier.weight(1f)
                )
            }

            // Plant Profile & Quiz Match Summary
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ForestGreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "My Botanical Profile",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark
                            )
                        }

                        TextButton(onClick = onNavigateToQuiz) {
                            Text("Update", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (quizPreferences.space.isNotBlank())
                            "Primary Space: ${quizPreferences.space} • Light: ${quizPreferences.sunlight} • Schedule: ${quizPreferences.careFrequency}"
                        else
                            "Take the Plant Match Quiz to calibrate personalized botanical recommendations for your home.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreenDark
                    )
                }
            }

            // Action Shortcuts
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    DashboardActionRow("Order History & Tracking", Icons.Default.LocalShipping, onNavigateToOrders)
                    DashboardActionRow("Plant Care Reminders", Icons.Default.Notifications, onNavigateToReminders)
                    DashboardActionRow("Saved Wishlist Items", Icons.Default.Favorite, onNavigateToWishlist)
                    DashboardActionRow("Switch to Admin Management", Icons.Default.AdminPanelSettings, { viewModel.toggleAdminMode() })
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(user.name) }
        var editEmail by remember { mutableStateOf(user.email) }
        var editPhone by remember { mutableStateOf(user.phone) }
        var editAddress by remember { mutableStateOf(user.defaultAddress) }
        var editCity by remember { mutableStateOf(user.city) }
        var editPostal by remember { mutableStateOf(user.postalCode) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text("Edit Profile & Address", style = MaterialTheme.typography.headlineSmall, color = ForestGreenDark)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("Phone") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = editAddress, onValueChange = { editAddress = it }, label = { Text("Address") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = editCity, onValueChange = { editCity = it }, label = { Text("City") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfile(editName, editEmail, editPhone, editAddress, editCity, editPostal)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DashboardMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ForestGreenDark)
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun DashboardActionRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = ForestGreenDark)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}
