package com.example.soundapp.Layout.Main.Sort

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Sort_QuranPreferences(private val context: Application?) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
        name = "music_pref"
    )

    companion object{
        val SORT_ORDER_KEY= stringPreferencesKey(name = "sort_order")

    }

    suspend fun saveSortOfQuran(id: String){
        context?.dataStore?.edit {
            //dataStore  معناه هيحفظ ال id في ال

              //  preferences -> preferences[LAST_PLAYED_SONG_KEY] = id
              it[SORT_ORDER_KEY] = id
        }

    }

    val Sorted_By: Flow<String>? = context?.dataStore?.data?.map{
            preferences ->
            preferences[SORT_ORDER_KEY] ?: "Name"
        }



}
