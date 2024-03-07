package com.example.mininghelper.screens.second.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MinerDao {

    @Insert//(onConflict = OnConflictStrategy.ABORT)
    fun insert(miner: Miner)

    @Query("SELECT * FROM miners")
    fun getAllMiner(): Flow<List<Miner>>

    @Query("DELETE FROM miners WHERE ${Miner.ID} = :id")
    fun deleteById(id: Int?)

    @Update
    fun update(miner: Miner)

}