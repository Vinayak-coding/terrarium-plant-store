package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.DeliveryOption
import com.example.data.model.MaintenanceLevel
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentMethod
import com.example.data.model.PlantSize
import com.example.data.model.ProductCategory
import com.example.data.model.ReminderType
import com.example.data.model.SunlightRequirement

class TerrariumConverters {
    @TypeConverter
    fun fromCategory(category: ProductCategory): String = category.name

    @TypeConverter
    fun toCategory(value: String): ProductCategory = try {
        ProductCategory.valueOf(value)
    } catch (e: Exception) {
        ProductCategory.INDOOR_PLANTS
    }

    @TypeConverter
    fun fromPlantSize(size: PlantSize): String = size.name

    @TypeConverter
    fun toPlantSize(value: String): PlantSize = try {
        PlantSize.valueOf(value)
    } catch (e: Exception) {
        PlantSize.MEDIUM
    }

    @TypeConverter
    fun fromSunlight(sunlight: SunlightRequirement): String = sunlight.name

    @TypeConverter
    fun toSunlight(value: String): SunlightRequirement = try {
        SunlightRequirement.valueOf(value)
    } catch (e: Exception) {
        SunlightRequirement.MEDIUM_INDIRECT
    }

    @TypeConverter
    fun fromMaintenance(maintenance: MaintenanceLevel): String = maintenance.name

    @TypeConverter
    fun toMaintenance(value: String): MaintenanceLevel = try {
        MaintenanceLevel.valueOf(value)
    } catch (e: Exception) {
        MaintenanceLevel.EASY
    }

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = try {
        OrderStatus.valueOf(value)
    } catch (e: Exception) {
        OrderStatus.ORDER_PLACED
    }

    @TypeConverter
    fun fromDeliveryOption(option: DeliveryOption): String = option.name

    @TypeConverter
    fun toDeliveryOption(value: String): DeliveryOption = try {
        DeliveryOption.valueOf(value)
    } catch (e: Exception) {
        DeliveryOption.STANDARD_DELIVERY
    }

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name

    @TypeConverter
    fun toPaymentMethod(value: String): PaymentMethod = try {
        PaymentMethod.valueOf(value)
    } catch (e: Exception) {
        PaymentMethod.ONLINE_CARD_UPI
    }

    @TypeConverter
    fun fromReminderType(type: ReminderType): String = type.name

    @TypeConverter
    fun toReminderType(value: String): ReminderType = try {
        ReminderType.valueOf(value)
    } catch (e: Exception) {
        ReminderType.WATERING
    }
}
