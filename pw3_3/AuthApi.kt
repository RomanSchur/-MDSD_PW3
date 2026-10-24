package com.example.pw3_3.basic
import retrofit2.http.Body
import retrofit2.http.POST
interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
