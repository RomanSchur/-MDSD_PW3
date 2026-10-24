package com.example.pw3_1.advanced
class CompensationRepository(
    private val local: LocalCompensationDataSource,
    private val remote: RemoteCompensationDataSource
) {
    private val cacheTTL = 5 * 60 * 1000L
    private var lastFetchTime = 0L
    suspend fun getAll(forceRefresh: Boolean = false): Result<List<CompensationNode>> {
        val currentTime = System.currentTimeMillis()
        val localData = local.getAll()

        if (!forceRefresh && localData.isNotEmpty() && (currentTime - lastFetchTime) < cacheTTL) {
            return Result.success(localData)
        }
        return try {
            val remoteResult = remote.fetchAll()
            if (remoteResult.isSuccess) {
                val remoteNodes = remoteResult.getOrNull() ?: emptyList()
                if (remoteNodes.isNotEmpty()) {
                    local.clear()
                    remoteNodes.forEach { local.insert(it) }
                }
                lastFetchTime = currentTime
                Result.success(local.getAll())
            } else {
                Result.success(localData)
            }
        } catch (e: Exception) {
            Result.success(local.getAll())
        }
    }
    suspend fun add(node: CompensationNode): Result<CompensationNode> {
        return try {
            val remoteResult = remote.addNode(node)
            if (remoteResult.isSuccess) {
                val added = remoteResult.getOrNull()!!
                local.insert(added)
                Result.success(added)
            } else {
                local.insert(node)
                Result.success(node)
            }
        } catch (e: Exception) {
            local.insert(node)
            Result.success(node)
        }
    }
    suspend fun update(node: CompensationNode): Result<CompensationNode> = try {
        val updated = remote.updateNode(node.id, node).getOrNull() ?: node
        local.update(updated)
        Result.success(updated)
    } catch (e: Exception) {
        local.update(node)
        Result.success(node)
    }
    suspend fun delete(id: Int): Result<Boolean> = try {
        remote.deleteNode(id)
        local.delete(id)
        Result.success(true)
    } catch (e: Exception) {
        local.delete(id)
        Result.success(true)
    }
    fun filterByName(query: String): List<CompensationNode> = local.filterByName(query)
    fun sortByPower(descending: Boolean): List<CompensationNode> = local.sortByPower(descending)
}