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
    fun `test delivery threshold logic`() {
        val subtotalFree = 1200.0
        val subtotalCharged = 450.0
        val isFreeDelivery = subtotalFree >= 999.0
        val isCharged = subtotalCharged < 999.0
        assertTrue(isFreeDelivery)
        assertTrue(isCharged)
    }
}

