package com.example.mininghelper

import android.app.Application
import com.example.mininghelper.screens.second.data.AppDatabase
import com.example.mininghelper.screens.second.data.MinerRepository
import com.example.mininghelper.screens.second.data.MinerRepositoryImpl

class App: Application() {
    private lateinit var database: AppDatabase

    lateinit var repository: MinerRepository

    override fun onCreate(){
        super.onCreate()
        database = AppDatabase.buildDatabase(applicationContext, DATABASE_NAME)
        repository = MinerRepositoryImpl(database.minerDao())
    }

    companion object {
        private const val DATABASE_NAME = "miners.db"
    }
}