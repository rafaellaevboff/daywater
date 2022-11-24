package com.rafaella.daywater

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.rafaella.daywater.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var waterManager : WaterManager

    // At the top level of your kotlin file:
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        waterManager = WaterManager(this)
        initListener()

        binding.diminui.setOnClickListener{
            val qtd = binding.qtdCopos.text.toString().toInt()
            val ml = binding.qtdMl.text.toString().toInt()

            if(qtd == 0) {
                Log.e("TAG" , "qtd 0")
            }else{
                var valorQtd = qtd - 1
                var valorMl = ml - 250
                binding.qtdCopos.text = valorQtd.toString()
                binding.qtdMl.text = valorMl.toString()
                Log.e("TAG" , "clicou exclui")
            }
        }

        binding.aumenta.setOnClickListener{
            val qtd = binding.qtdCopos.text.toString().toInt()
            val ml = binding.qtdMl.text.toString().toInt()

            var valorQtd = qtd + 1
            var valorMl = ml + 250

            binding.qtdCopos.text = valorQtd.toString()
            binding.qtdMl.text = valorMl.toString()
            Log.e("TAG" , "clicou add")
        }

        readData()
    }

    fun initListener(){
        binding.save.setOnClickListener{saveData()}
    }

     fun saveData(){
        val qtd = binding.qtdCopos.text.toString().toInt()
        val ml = binding.qtdMl.text.toString().toInt()

        lifecycleScope.launch{
            waterManager.saveData(qtd, ml)
        }
    }

    fun deletarDados(){
        val dataHoraAtual = Date()
        val hora = SimpleDateFormat("HH:mm:ss").format(dataHoraAtual)
        val data = SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual)
        Log.e("TAG", hora)
        if(hora == "24:00:00"){
            lifecycleScope.launch{
                waterManager.deleteData()
                binding.qtdCopos.text = "0"
                binding.qtdMl.text = "0"
            }
        }
    }

     fun readData(){
        lifecycleScope.launch{
            val water = waterManager.readData()

            binding.qtdCopos.text = water.qtd.toString()
            binding.qtdMl.text = water.ml.toString()
        }
    }

    override fun onResume() {
        super.onResume()

    }
}
