package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SeedData
import com.example.data.model.DeliveryOption
import com.example.data.model.PaymentMethod
import com.example.data.model.QuizPreferences
import com.example.data.model.UserAccount
import com.example.data.model.formatRupees
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Terrarium", appName)
    }

    @Test
    fun `test formatRupees utility`() {
        assertEquals("₹499", formatRupees(499.0))
        assertEquals("₹1,250", formatRupees(1250.0))
        assertEquals("₹0", formatRupees(0.0))
    }

    @Test
    fun `test initial seed data exists`() {
        val products = SeedData.initialProducts
        assertTrue(products.isNotEmpty())
        assertTrue(products.any { it.name.contains("Snake Plant", ignoreCase = true) })
        assertTrue(products.any { it.name.contains("Money Plant", ignoreCase = true) })
    }

    @Test
    fun `test nav destination care route`() {
        assertEquals("care", com.example.ui.components.NavDestination.CARE.route)
    }

    @Test
    fun `test neutral first-time customer profile`() {
        val user = UserAccount()
        assertEquals("New Customer", user.name)
        assertEquals("customer@example.com", user.email)
        assertEquals("New Plant Parent", user.membershipTier)
        assertEquals("Thane", user.city)
        assertEquals("Maharashtra", user.state)
        assertEquals("400606", user.pinCode)
    }

    @Test
    fun `test admin profile separation`() {
        val admin = com.example.data.model.AdminProfile()
        assertEquals("Store Admin", admin.name)
        assertEquals("Thane, Maharashtra", admin.storeLocation)
        assertEquals("[Store Name]", admin.storeName)
    }

    @Test
    fun `test store pickup delivery option`() {
        val pickup = DeliveryOption.STORE_PICKUP
        assertTrue(pickup.displayName.contains("Thane"))
        assertEquals(0.0, pickup.fee, 0.01)
    }

    @Test
    fun `test initial care reminders is empty for first-time user`() {
        assertTrue(SeedData.initialReminders.isEmpty())
    }
}

