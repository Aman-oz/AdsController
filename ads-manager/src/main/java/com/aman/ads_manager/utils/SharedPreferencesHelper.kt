package com.aman.ads_manager.utils

import android.content.Context

object SharedPreferencesHelper {

    const val PREF_NAME = "AdsController"
    const val IS_APP_PURCHASED = "IS_APP_PURCHASED"
    const val IS_INTERSTITIAL_AD_SHOWING = "IS_INTERSTITIAL_AD_SHOWING"

    fun setBoolean(context: Context, key: String, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, default)
    }
}