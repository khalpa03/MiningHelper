package com.example.mininghelper.screens.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mininghelper.databinding.ItemMinerBinding
import com.example.mininghelper.screens.second.data.Miner

class MinerAdapter(private val clickListener: OnItemClickListener): RecyclerView.Adapter<MinerViewHolder>() {

    private val minerList = mutableListOf<Miner>()
    val editTextContent = MutableLiveData<String>()

    fun addMiner(miner: Miner){
        minerList.add(miner)
        notifyDataSetChanged()
    }

    fun setMiner(miner: List<Miner>) {
        minerList.clear()
        minerList.addAll(miner)
        notifyDataSetChanged()

    }

    fun removeMiner(id: Int?){
        val minerToRemove = minerList.find{it.id == id}
        minerList.remove(minerToRemove)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinerViewHolder {
        val binding = ItemMinerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MinerViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return minerList.size
    }

    override fun onBindViewHolder(holder: MinerViewHolder, position: Int) {
        holder.bind(minerList[position])
    }

    interface OnItemClickListener {
        abstract val applicationContext: Any
        fun onItemClicked(id: Int?)
        fun onSaveClicked(id: Int?, name: String, power: String, hashrate: String, price: String)
    }

}