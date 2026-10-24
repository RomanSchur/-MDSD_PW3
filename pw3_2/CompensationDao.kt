package com.example.pw3_2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompensationDao {

    @Query("SELECT * FROM compensation_nodes")
    fun getAllNodes(): Flow<List<CompensationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: CompensationEntity)

    @Delete
    suspend fun deleteNode(node: CompensationEntity)
}