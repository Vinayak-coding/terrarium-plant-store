package com.example.data.model

data class CareGuide(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val readTimeMinutes: Int,
    val summary: String,
    val plantType: String,
    val sections: List<GuideSection>,
    val quickTips: List<String>
)

data class GuideSection(
    val heading: String,
    val body: String
)
