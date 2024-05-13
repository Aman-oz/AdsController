package com.aman.adscontroller.utils

import android.content.Context

object SharedPreferencesHelper {

    private const val PREF_NAME = "AdsController"
    private const val IS_APP_PURCHASED = "IS_APP_PURCHASED"

    fun setBoolean(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_APP_PURCHASED, value)
        editor.apply()
    }

    fun getBoolean(context: Context, default: Boolean = false): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_APP_PURCHASED, default)
    }
}