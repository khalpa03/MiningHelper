package com.example.mininghelper.screens.second.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "miners"
)

data class Miner(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "power")
    val power: String,
    @ColumnInfo(name = "hashrate")
    val hashrate: String,
    @ColumnInfo(name = "price")
    val price: String)

{
    companion object {
        const val TABLE_NAME = "Miner"
        const val ID = "Id"
        const val NAME = "Name"
        const val POWER = "Power"
        const val HASHRATE = "Hashrate"
        const val PRICE = "Price"
    }
}