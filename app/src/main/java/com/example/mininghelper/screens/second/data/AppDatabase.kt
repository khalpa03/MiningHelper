package com.example.mininghelper.screens.second.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Miner::class], version = 1)

abstract class AppDatabase: RoomDatabase() {

    abstract fun minerDao(): MinerDao

    companion object {
        fun buildDatabase(context: Context, dbName: String): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "miners.db").build()
        }
    }
}