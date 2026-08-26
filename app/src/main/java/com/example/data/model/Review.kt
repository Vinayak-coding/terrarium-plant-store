package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val dateText: String,
    val plantSetupPhoto: String? = null,
    val isApproved: Boolean = true,
    val helpfulCount: Int = 3
)
