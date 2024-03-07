package com.example.mininghelper.screens.second.data

import kotlinx.coroutines.flow.Flow

class MinerRepositoryImpl(private val minerDao: MinerDao): MinerRepository {
    override fun insert(miner: Miner) {
        minerDao.insert(miner)
    }

    override fun getAllMiner(): Flow<List<Miner>> {
        return minerDao.getAllMiner()
    }

    override fun deleteById(id: Int?) {
        minerDao.deleteById(id)
    }

    override fun update(miner: Miner){
        minerDao.update(miner)
    }

}