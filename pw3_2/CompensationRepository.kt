package com.example.pw3_2.room

import kotlinx.coroutines.flow.Flow

class CompensationRepository(private val dao: CompensationDao) {
    val allNodes: Flow<List<CompensationEntity>> = dao.getAllNodes()

    suspend fun insert(node: CompensationEntity) {
        dao.insertNode(node)
    }

    suspend fun delete(node: CompensationEntity) {
        dao.deleteNode(node)
    }
}