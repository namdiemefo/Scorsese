package com.naemo.scorsese.data.local

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

class AppPreferencesHelper(val context: Context) {

    val SHARED_PREFS: String = "sharedPrefs"
    private var mInstance: AppPreferencesHelper? = null

    @Synchronized
    fun getInstance(context: Context): AppPreferencesHelper {
        if (mInstance == null) {
            mInstance = AppPreferencesHelper(context)
        }
        return mInstance as AppPreferencesHelper
    }

    fun saveButtonState(state: Boolean) {
        val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()

        editor.putBoolean("added", state)
        Toast.makeText(context, "done did", Toast.LENGTH_LONG).show()
        editor.apply()
    }

    fun getButtonState(): Boolean {
        val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return preferences.getBoolean("added", false)
    }

}