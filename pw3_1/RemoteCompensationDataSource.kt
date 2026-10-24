package com.example.pw3_1.advanced
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RemoteCompensationDataSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5156/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(CompensationApiService::class.java)
    suspend fun fetchAll(): Result<List<CompensationNode>> = try {
        Result.success(service.getSubstations())
    } catch (e: Exception) {
        Result.failure(e)
    }
    suspend fun addNode(node: CompensationNode): Result<CompensationNode> = try {
        val response = service.addSubstation(node)
        if (response.isSuccessful) Result.success(response.body()!!)
        else Result.failure(Exception("Add failed: ${response.code()}"))
    } catch (e: Exception) {
        Result.failure(e)
    }
    suspend fun updateNode(id: Int, node: CompensationNode): Result<CompensationNode> = try {
        val response = service.updateSubstation(id, node)
        if (response.isSuccessful) Result.success(response.body()!!)
        else Result.failure(Exception("Update failed: ${response.code()}"))
    } catch (e: Exception) {
        Result.failure(e)
    }
    suspend fun deleteNode(id: Int): Result<Boolean> = try {
        val response = service.deleteSubstation(id)
        if (response.isSuccessful) Result.success(true)
        else Result.failure(Exception("Delete failed: ${response.code()}"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}