package com.aman.ads_manager.callbacks

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
interface RewardedOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onPreloaded()
}