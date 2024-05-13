package com.aman.ads_manager.callbacks

/**
 * @Author: Aman Ullah
 * @Date: 06,May,2024.
 * Software Engineer Android
 */
interface BannerCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdImpression()
    fun onPreloaded()
    fun onAdClicked()
    fun onAdClosed()
    fun onAdOpened()
}