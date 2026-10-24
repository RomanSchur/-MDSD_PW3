package com.example.pw3_2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CompensationEntity::class], version = 1, exportSchema = false)
abstract class CompensationDatabase : RoomDatabase() {

    abstract fun compensationDao(): CompensationDao

    companion object {
        @Volatile
        private var INSTANCE: CompensationDatabase? = null

        fun getDatabase(context: Context): CompensationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CompensationDatabase::class.java,
                    "compensation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}