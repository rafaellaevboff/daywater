package com.rafaella.daywater

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class WaterManager (val context : Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "water")

    companion object {
        private val WATER_QTD_KEY = intPreferencesKey("AGUA_QTD")
        private val WATER_ML_KEY = intPreferencesKey("AGUA_ML")
    }

    suspend fun saveData(qtd: Int, ml : Int){
        context.dataStore.edit{
            it[WATER_QTD_KEY] = qtd
            it[WATER_ML_KEY] = ml
        }
    }

    suspend fun readData(): Water{
        val prefs = context.dataStore.data.first()

        return Water(
            qtd = prefs[WATER_QTD_KEY] ?: 0,
            ml = prefs[WATER_ML_KEY] ?: 0
        )
    }

    suspend fun deleteData(){
        context.dataStore.edit{
            it[WATER_QTD_KEY] = 0
            it[WATER_ML_KEY] = 0
        }
    }
}