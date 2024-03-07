package com.example.mininghelper.screens.second

import android.text.Editable
import androidx.recyclerview.widget.RecyclerView
import com.example.mininghelper.databinding.ItemMinerBinding
import com.example.mininghelper.screens.second.data.Miner


class MinerViewHolder(private val binding: ItemMinerBinding, private val clickListener: MinerAdapter.OnItemClickListener): RecyclerView.ViewHolder(binding.root) {

    fun bind(miner: Miner) {
        binding.nameEditView.text = Editable.Factory.getInstance().newEditable(miner.name)
        binding.PowerEditView.text = Editable.Factory.getInstance().newEditable(miner.power)
        binding.HashrateEditView.text = Editable.Factory.getInstance().newEditable(miner.hashrate.toString())
        binding.PriceEditView.text = Editable.Factory.getInstance().newEditable(miner.price.toString())

        binding.imageDelete.setOnClickListener{
            clickListener.onItemClicked(miner.id)
        }
        binding.imageSave.setOnClickListener{
            val id = miner.id
            val name = binding.nameEditView.text.toString()
            val power = binding.PowerEditView.text.toString()
            val hashrate = binding.HashrateEditView.text.toString()
            val price = binding.PriceEditView.text.toString()
            clickListener.onSaveClicked(id, name, power, hashrate, price)
        }
    }
}
