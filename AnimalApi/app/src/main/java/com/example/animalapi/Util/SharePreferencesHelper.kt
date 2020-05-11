package com.example.animalapi.Util

import android.content.Context
import android.content.SharedPreferences

import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharePreferencesHelper {
    companion object {
        private const val PREF_TIME = "KEY"
        private var pref: SharedPreferences? = null

        @Volatile
        private var instance: SharePreferencesHelper? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildSharePreference(context).also {
                instance = it
            }
        }

        private fun buildSharePreference(context: Context): SharePreferencesHelper {
            pref = PreferenceManager.getDefaultSharedPreferences(context)
            return SharePreferencesHelper()
        }
    }

    fun saveUpdateKey(key: String) {
        pref?.edit(commit = true) {
            putString(PREF_TIME, key)
        }
    }

    fun getUpdateKey(): String? = pref?.getString(PREF_TIME, null)
}