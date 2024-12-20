package com.aman.ads_manager.callbacks

import com.google.android.gms.ads.AdValue

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
interface InterstitialOnShowCallBack {
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
    fun onAdShowedFullScreenContent()
    fun onAdImpression()
    fun onAdNull()
    fun onPaidEvent(adValue: AdValue)
}