package com.example.mininghelper.screens.second

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mininghelper.App
import com.example.mininghelper.databinding.FragmentSecondBinding
import com.example.mininghelper.databinding.ItemMinerBinding
import com.example.mininghelper.screens.second.data.Miner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFragment : Fragment(), MinerAdapter.OnItemClickListener {

    override val applicationContext: Context
        get() = requireContext()


    private lateinit var minerAdapter: MinerAdapter

    private var alertDialog: AlertDialog? = null

    private var _binding: FragmentSecondBinding? = null
    private var _binding1: ItemMinerBinding? = null
    // Это свойство доступно только между onCreateView и onDestroyView.
    private val binding get() = _binding!!
    private val binding1 get() = _binding1!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        _binding1 = ItemMinerBinding.inflate(inflater, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        savedInstanceState?.getString("editTextContent")?.let {
            binding1.nameEditView.setText(it)
            Log.d("EditText2", it);}
        binding.floatingActionButton.setOnClickListener {
            val miner = Miner(
                null,
                name = "",
                power = "",
                hashrate = "",
                price = "",
            )

            minerAdapter.addMiner(miner)
            insertMiner(miner)

        }
        retrieveMiner()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("editTextContent", binding1.nameEditView.text.toString())
        Log.d("EditText", outState.toString());
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(id: Int?) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete miner")
            .setMessage("Do you want to delete the miner from the list")
            .setPositiveButton("Yes"){dialog, which ->
                Thread{
                    deleteMinerById(id)
                }.start()
                dialog.dismiss()
            }.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSaveClicked(id: Int?, name: String, power: String, hashrate: String, price: String){
        val miner = Miner(
            id = id,
            name = name,
            power = power,
            hashrate = hashrate,
            price = price,
        )
//        lifecycleScope.launch(Dispatchers.IO) {
//            // Сохранение в БД
//            (requireContext().applicationContext as App).repository.insert(miner = miner)
//            // Обновление списка из БД
//            retrieveMiner()
//        }
        //val minerToUpdate = Miner( name = "Updated Name", power = "New Power", hashrate = "Updated Hashrate", price = "New Price")
        lifecycleScope.launch(Dispatchers.IO) {

            (requireContext().applicationContext as App).repository.update(miner = miner)
        }
    }


    private fun initRecyclerView() {
        minerAdapter = MinerAdapter(this)

        with(binding.minerList) {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = minerAdapter
            this.setHasFixedSize(true)
        }
    }

    private fun insertMiner(miner: Miner){
        lifecycleScope.launch(Dispatchers.IO) {

            (requireContext().applicationContext as App).repository.insert(miner = miner)
        }
    }

    private fun retrieveMiner(){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
            val miners = (requireContext().applicationContext as App).repository.getAllMiner()
            miners.collect { minersList ->
                withContext(Dispatchers.Main) {
                    minerAdapter.setMiner(minersList)

                }
            }

        } catch (e: Exception) {
            Log.e("SecondFragment", "Error retrieving miners", e)
        }
    }
    }

    private fun deleteMinerById(id: Int?){
        lifecycleScope.launch(Dispatchers.IO) {
            (requireContext().applicationContext as App).repository.deleteById(id)
            withContext(Dispatchers.Main){
                minerAdapter.removeMiner(id)
            }
        }
    }

}

