package com.example.mininghelper.screens.first

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mininghelper.R
import com.example.mininghelper.databinding.ActivityMainBinding
import com.example.mininghelper.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    // Это свойство доступно только между onCreateView и onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установите адаптер для Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.currencySpinner.adapter = adapter
        }

        // Установите слушатель выбора элементов
        binding.currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCurrency = parent.getItemAtPosition(position).toString()
                Log.d("MyLog", "Selected currency: $selectedCurrency")
                if (selectedCurrency == "USD")
                {
                    calculate(selectedCurrency)
                }
                if (selectedCurrency == "RUB"){
                    calculate(selectedCurrency)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface ResponseCallback {
        fun onResponse(response: String)
    }
    interface ConverterCallback {
        fun onSuccess(money: String)
    }

    private fun calculate(valute: String){
        if (valute == "RUB"){
            binding.textCurrencyW.text = "rub/кВТ"
        }
        else if(valute == "USD"){
            binding.textCurrencyW.text = "usd/кВТ"
        }
        binding.calculateButton.setOnClickListener {
            val hashrateStr = binding.hashrateMiner.text.toString()
            if (hashrateStr.isBlank()){
                    val shake = AnimationUtils.loadAnimation(context, R.anim.shake_animation)
                    binding.hashrateMiner.startAnimation(shake)
            }else {
                getResponse(object : ResponseCallback {
                    override fun onResponse(response: String) {
                        var difficulty = response.toDouble()
                        var difficultyLong = difficulty.toLong()
                        val hashrate = binding.hashrateMiner.text.toString().toDouble()
                        val selectedId = binding.tipOptions.checkedRadioButtonId
                        val ThorMh = when(selectedId){
                            R.id.option_TH -> 1000000000000
                            else -> 10000000000
                        }
                        var result =
                            (((hashrate * ThorMh * 86400) / (difficultyLong * 65536)) / 65536) * 6.25
                        val formattedRes = String.format("%.8f", result)
                        binding.textBtc.text = getString(R.string.str_income, formattedRes)
                        binding.textIncome.visibility = View.VISIBLE

                        val expenses = expenses()
                        if (expenses != 0.0) {
                            val formattedExp = String.format("%.2f", expenses)
                            if (valute == "RUB") {
                                binding.textElectric.text =
                                    getString(R.string.str_exp_rub, formattedExp)
                                binding.textExpenses.visibility = View.VISIBLE
                                val exp = formattedExp.replace(",", ".")
                                converter("https://blockchain.info/tobtc?currency=RUB&value=$exp", object : ConverterCallback {
                                    override fun onSuccess(money: String) {
                                        val profit = String.format("%.8f", formattedRes.replace(",", ".").toDouble() - money.toDouble())
                                        val btctorub = String.format("%.2f",((profit.replace(",", ".").toDouble()) * (exp.toDouble() / money.toDouble())))
                                        binding.textProfitbtc.text = getString(R.string.str_exp_rub, profit + "₿ ≈ " + btctorub)
                                        binding.textProfit.visibility = View.VISIBLE
                                    }
                                })
                            } else if (valute == "USD") {
                                binding.textElectric.text =
                                    getString(R.string.str_exp_usd, formattedExp)
                                binding.textExpenses.visibility = View.VISIBLE
                                val exp = formattedExp.replace(",", ".")
                                converter("https://blockchain.info/tobtc?currency=USD&value=$exp", object : ConverterCallback {
                                    override fun onSuccess(money: String) {
                                        val profit = String.format("%.8f", formattedRes.replace(",", ".").toDouble() - money.toDouble())
                                        val btctousd = String.format("%.2f",((profit.replace(",", ".").toDouble()) * (exp.toDouble() / money.toDouble())))
                                        binding.textProfitbtc.text = getString(R.string.str_exp_usd, profit + "₿ ≈ " + btctousd)
                                        binding.textProfit.visibility = View.VISIBLE
                                    }
                                })
                            }
                        }
                    }
                })
            }
        }
    }

    private  fun expenses(): Double{
        try{
            val power = binding.powerMiner.text.toString().toDouble()
            val tariff = binding.tariff.text.toString().toDouble()
            val result = tariff * (power / 1000) * 24
            return result
        }catch (exception: Exception){
            return 0.0
        }
    }

    private fun converter(url: String, callback: ConverterCallback){
        val queue = Volley.newRequestQueue(this.context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    val money = response.toDouble()
                    callback.onSuccess(response)
                    Log.d("MyLog", "Money: $money")
                } catch (e: NumberFormatException) {
                    Log.d("MyLog", "Failed to parse response money: $e")
                }
            },
            { error ->
                Log.d("MyLog", "Volley error: ${error.message}")
            }
        )
        queue.add(stringRequest)
    }

    private fun getResponse(callback: ResponseCallback){
        val url = "https://blockchain.info/q/getdifficulty"
        val queue = Volley.newRequestQueue(this.context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response -> callback.onResponse(response)
                // Попытка преобразовать ответ в Double
                try {
                    val difficulty = response.toDouble()
                    Log.d("MyLog", "Difficulty: $difficulty")
                } catch (e: NumberFormatException) {
                    Log.d("MyLog", "Failed to parse response: $e")
                }
            },
            { error ->
                Log.d("MyLog", "Volley error: ${error.message}")
            }
        )
        queue.add(stringRequest)
    }
}