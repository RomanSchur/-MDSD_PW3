package com.example.pw3_2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compensation_nodes")
data class CompensationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val substationName: String,
    val voltageKv: Double,
    val reactivePowerQ: Double,
    val activeSteps: Int
)