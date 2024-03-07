package com.example.mininghelper.screens.second.data

import kotlinx.coroutines.flow.Flow

interface MinerRepository {
    fun insert(miner: Miner)

    fun getAllMiner(): Flow<List<Miner>>

    fun deleteById(id: Int?)

    fun update(miner: Miner)
}