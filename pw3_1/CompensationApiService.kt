package com.example.pw3_1.advanced
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompensationApiService {
    @GET("substations")
    suspend fun getSubstations(): List<CompensationNode>

    @POST("substations")
    suspend fun addSubstation(@Body node: CompensationNode): Response<CompensationNode>

    @PUT("substations/{id}")
    suspend fun updateSubstation(@Path("id") id: Int, @Body node: CompensationNode): Response<CompensationNode>

    @DELETE("substations/{id}")
    suspend fun deleteSubstation(@Path("id") id: Int): Response<Unit>
}