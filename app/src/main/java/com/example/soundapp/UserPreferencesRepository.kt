package com.example.soundapp
//
//import android.content.Context
//import android.provider.Settings.Global.putString
//import androidx.datastore.core.DataStore
//import androidx.datastore.migrations.SharedPreferencesMigration
//import androidx.datastore.preferences.SharedPreferencesMigration
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.emptyPreferences
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.catch
//import java.io.IOException
//
//class UserPreferencesRepository(context: Context) {
//
//    private val sharedPreferences =
//        context.applicationContext.
//        getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
//
//    private val _userPreferencesFlow = MutableStateFlow(userPreferences)
//    val userPreferencesFlowe: StateFlow<UserPreferences> = _userPreferencesFlow
//
//    private val userPreferences: UserPreferences
//        get() {
//            val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
//            val filter = sharedPreferences.getString(FILTER_KEY, Filter.NONE.name)
//            return UserPreferences(
//                sortOrder = SortOrder.valueOf(order ?: SortOrder.NONE.name),
//                filter = Filter.valueOf(filter ?: Filter.NONE.name)
//            )
//        }
//
////    fun updateFilter(filter: Filter) {
////        _userPreferencesFlow.value = userPreferences.copy(filter = filter)
////        sharedPreferences .edit {
////            putString(FILTER_KEY, filter.name) }
////    }
////
////    fun updateSortOrder(sortOrder: SortOrder) {
////        _userPreferencesFlow.value = userPreferences.copy(sortOrder = sortOrder)
////        sharedPreferences.edit {
////            putString(SORT_ORDER_KEY, sortOrder.name)
////        }
////    }
//
//    companion object {
//        private const val USER_PREFERENCES_NAME = "user_preferences"
//        private const val SORT_ORDER_KEY = "sort_order"
//        private const val FILTER_KEY = "filter"
//    }
//
//
//
//    val dataStore: DataStore<Preferences> = context.applicationContext.createDataStore(
//        name = USER_PREFERENCES_NAME,
//        migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME))
//    )
//    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preferences ->
//            UserPreferences(
//                sortOrder = SortOrder.valueOf(preferences[SORT_ORDER] ?: SortOrder.NONE.name),
//                filter = Filter.valueOf(preferences[FILTER] ?: Filter.NONE.name)
//            )
//        }
//}
//
//
//enum class SortOrder {
//    NONE,
//    BY_NAME,
//    BY_YEAR,
//    BY_RATING
//
//
//}
//
//enum class Filter {
//    NONE,
//    HORROR,
//    ACTION,
//    DRAMA
//}
//
//data class UserPreferences(val sortOrder: SortOrder, val filter: Filter)
