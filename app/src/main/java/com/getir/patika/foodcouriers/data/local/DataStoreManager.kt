package com.getir.patika.foodcouriers.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val dataStore: DataStore<Preferences>) {
    val gson = Gson()
    companion object {
        val TOKEN = stringPreferencesKey("user_id")
        val FOODS = stringPreferencesKey("foods")
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = userId
        }
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN]
    }

    suspend fun deleteUserId() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN)
        }
    }


}