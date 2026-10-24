package com.example.pw3_1.advanced
data class CompensationNode(
    val id: Int,
    val substationName: String,
    val voltageKv: Double,
    val reactivePowerQ: Double,
    val activeSteps: Int
)