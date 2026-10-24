package com.example.pw3_4.basic

import retrofit2.http.GET

interface NetworkApiService {
    @GET("network/reactive-power")
    suspend fun getReactivePowerData(): ReactivePowerData
}