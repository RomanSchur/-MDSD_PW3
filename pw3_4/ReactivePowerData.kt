package com.example.pw3_4.basic
import com.google.gson.annotations.SerializedName

data class ReactivePowerData(
    @SerializedName("date") val date: String,
    @SerializedName("reactivePowerKvar") val reactivePowerKvar: Double,
    @SerializedName("powerFactor") val powerFactor: Double,
    @SerializedName("status") val status: String
)