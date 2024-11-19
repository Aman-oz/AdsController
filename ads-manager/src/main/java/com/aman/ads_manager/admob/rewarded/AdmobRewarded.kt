package com.aman.ads_manager.admob.rewarded

import android.app.Activity
import android.content.Context
import android.util.Log
import com.aman.ads_manager.callbacks.RewardedOnLoadCallBack
import com.aman.ads_manager.callbacks.RewardedOnShowCallBack
import com.aman.ads_manager.constants.AdsConstants.isRewardedLoading
import com.aman.ads_manager.constants.AdsConstants.rewardedAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
class AdmobRewarded(private val context: Context) {

    private val TAG = AdmobRewarded::class.java.simpleName

    companion object {
        var isRewardedShowing = false
    }
    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    fun loadRewardedAd(
        rewardedIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        listener: RewardedOnLoadCallBack
    ) {
        context?.let { mContext ->
            if (isInternetConnected && adEnable != 0 && !isAppPurchased && !isRewardedLoading && rewardedIds.isNotEmpty()) {
                if (rewardedAd == null) {
                    isRewardedLoading = true
                    RewardedAd.load(
                        mContext,
                        rewardedIds,
                        AdRequest.Builder().build(),
                        object : RewardedAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.e(TAG, "Rewarded onAdFailedToLoad: ${adError.message}")
                                isRewardedLoading = false
                                rewardedAd = null
                                listener.onAdFailedToLoad(adError.toString())
                            }

                            override fun onAdLoaded(ad: RewardedAd) {
                                Log.d(TAG, "Rewarded onAdLoaded")
                                isRewardedLoading = false
                                rewardedAd = ad
                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d(TAG, "admob Rewarded onPreloaded")
                    listener.onPreloaded()
                }

            } else {
                Log.e(TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                listener.onAdFailedToLoad("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    fun showRewardedAd(activity: Activity?, listener: RewardedOnShowCallBack) {
        activity?.let { mActivity ->
            if (rewardedAd != null) {
                rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d(TAG, "admob Rewarded onAdClicked")
                        listener.onAdClicked()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "admob Rewarded onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()
                        isRewardedShowing = false
                        rewardedAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "admob Rewarded onAdFailedToShowFullScreenContent: ${adError.message}")

                        isRewardedShowing = false
                        listener.onAdFailedToShowFullScreenContent()
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "admob Rewarded onAdShowedFullScreenContent")
                        listener.onAdShowedFullScreenContent()
                        isRewardedShowing = true
                        rewardedAd = null
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "admob Rewarded onAdImpression")
                        listener.onAdImpression()
                    }
                }
                rewardedAd?.let { ad ->
                    isRewardedShowing = true
                    ad.show(mActivity) { rewardItem ->
                        Log.d(TAG, "admob Rewarded onUserEarnedReward")
                        listener.onUserEarnedReward()
                    }

                    ad.onPaidEventListener = OnPaidEventListener { adValue ->
                        listener.onPaidEvent(adValue)
                    }
                }
            }
        }
    }

    fun isRewardedLoaded(): Boolean {
        return rewardedAd != null
    }

    fun dismissRewarded() {
        rewardedAd = null
    }
}