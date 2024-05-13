package com.aman.ads_manager.callbacks

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
interface RewardedOnShowCallBack {
    fun onAdClicked()
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
    fun onAdImpression()
    fun onAdShowedFullScreenContent()
    fun onUserEarnedReward()
}