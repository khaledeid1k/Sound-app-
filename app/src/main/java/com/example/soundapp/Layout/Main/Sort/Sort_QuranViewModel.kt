package com.example.soundapp.Layout.Main.Sort

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sort_QuranViewModel(application: Application)
    : AndroidViewModel(application){



    private val SortDataStore = Sort_QuranPreferences(application)

    // 1
    val getSort = SortDataStore.Sorted_By

    // 2
    fun saveToDataStore(SortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SortDataStore.saveSortOfQuran(SortBy)
        }

    }


}