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
class AdmobRewarded {

    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context
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
                                Log.e("AdsInformation", "Rewarded onAdFailedToLoad: ${adError.message}")
                                isRewardedLoading = false
                                rewardedAd = null
                                listener.onAdFailedToLoad(adError.toString())
                            }

                            override fun onAdLoaded(ad: RewardedAd) {
                                Log.d("AdsInformation", "Rewarded onAdLoaded")
                                isRewardedLoading = false
                                rewardedAd = ad
                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d("AdsInformation", "admob Rewarded onPreloaded")
                    listener.onPreloaded()
                }

            } else {
                Log.e("AdsInformation", "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                listener.onAdFailedToLoad("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    fun showRewardedAd(activity: Activity?, listener: RewardedOnShowCallBack) {
        activity?.let { mActivity ->
            if (rewardedAd != null) {
                rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d("AdsInformation", "admob Rewarded onAdClicked")
                        listener.onAdClicked()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d("AdsInformation", "admob Rewarded onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()
                        rewardedAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e("AdsInformation", "admob Rewarded onAdFailedToShowFullScreenContent: ${adError.message}")

                        listener.onAdFailedToShowFullScreenContent()
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("AdsInformation", "admob Rewarded onAdShowedFullScreenContent")
                        listener.onAdShowedFullScreenContent()
                        rewardedAd = null
                    }

                    override fun onAdImpression() {
                        Log.d("AdsInformation", "admob Rewarded onAdImpression")
                        listener.onAdImpression()
                    }
                }
                rewardedAd?.let { ad ->
                    ad.show(mActivity) { rewardItem ->
                        Log.d("AdsInformation", "admob Rewarded onUserEarnedReward")
                        listener.onUserEarnedReward()
                    }

                    ad.onPaidEventListener = OnPaidEventListener { adValue ->
                        //revenue logic here
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