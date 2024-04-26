package com.sds.pokemonguide

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPrefsManager(context: Context) {

    private var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getTheme() : Boolean = sharedPrefs.getBoolean(THEME_KEY, true)

    fun setTheme(boolean: Boolean) {
        val editor = sharedPrefs.edit()
        editor.putBoolean(THEME_KEY, boolean)
        editor.apply()
    }

    companion object {
        private const val THEME_KEY = "isDarkTheme"
    }
}