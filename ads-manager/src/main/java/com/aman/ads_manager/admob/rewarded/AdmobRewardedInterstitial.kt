package com.aman.ads_manager.admob.rewarded

import android.app.Activity
import android.content.Context
import android.util.Log
import com.aman.ads_manager.callbacks.RewardedOnLoadCallBack
import com.aman.ads_manager.callbacks.RewardedOnShowCallBack
import com.aman.ads_manager.constants.AdsConstants.isRewardedInterLoading
import com.aman.ads_manager.constants.AdsConstants.rewardedInterAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
class AdmobRewardedInterstitial(private val context: Context) {

    private val TAG = AdmobRewardedInterstitial::class.java.simpleName

//    private var context: Context? = null

    /*fun initialize(context: Context) {
        this.context = context
    }*/

    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    fun loadRewardedAd(
        rewardedIds: String,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        listener: RewardedOnLoadCallBack
    ) {
        context?.let { mContext ->
            if (isInternetConnected && !isAppPurchased && !isRewardedInterLoading && rewardedIds.isNotEmpty()) {
                if (rewardedInterAd == null) {
                    isRewardedInterLoading = true
                    RewardedInterstitialAd.load(
                        mContext,
                        rewardedIds,
                        AdRequest.Builder().build(),
                        object : RewardedInterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.e(
                                    TAG,
                                    "Rewarded onAdFailedToLoad: ${adError.message}"
                                )
                                isRewardedInterLoading = false
                                rewardedInterAd = null
                                listener.onAdFailedToLoad(adError.toString())
                            }

                            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                                Log.d(TAG, "Rewarded onAdLoaded")
                                isRewardedInterLoading = false
                                rewardedInterAd = ad
                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d(TAG, "admob Rewarded onPreloaded")
                    listener.onPreloaded()
                }

            } else {
                Log.e(
                    TAG,
                    "isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected"
                )
                listener.onAdFailedToLoad("isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    fun showRewardedAd(activity: Activity?, listener: RewardedOnShowCallBack) {
        activity?.let { mActivity ->
            if (rewardedInterAd != null) {
                rewardedInterAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d(TAG, "admob Rewarded onAdClicked")
                        listener.onAdClicked()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "admob Rewarded onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()
                        rewardedInterAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(
                            TAG,
                            "admob Rewarded Interstitial onAdFailedToShowFullScreenContent: ${adError.message}"
                        )
                        listener.onAdFailedToShowFullScreenContent()
                        rewardedInterAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(
                            TAG,
                            "admob Rewarded Interstitial onAdShowedFullScreenContent"
                        )
                        listener.onAdShowedFullScreenContent()
                        rewardedInterAd = null
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "admob Rewarded Interstitial onAdImpression")
                        listener.onAdImpression()
                    }
                }
                rewardedInterAd?.let { ad ->
                    ad.show(mActivity) { rewardItem ->
                        Log.d(TAG, "admob Rewarded Interstitial onUserEarnedReward")
                        listener.onUserEarnedReward()
                    }

                    ad.onPaidEventListener = OnPaidEventListener { adValue ->
                        //ad revenue logic goes here
                    }
                }
            }
        }
    }

    fun isRewardedLoaded(): Boolean {
        return rewardedInterAd != null
    }

    fun dismissRewarded() {
        rewardedInterAd = null
    }

    fun loadRewardedInterstitialAd(
        rewardedIds: String,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        listener: RewardedOnLoadCallBack
    ) {
        if (!isInternetConnected || isAppPurchased)
            return
        context?.let { mContext ->
            if (!isRewardedInterLoading && rewardedIds.isNotEmpty()) {
                if (rewardedInterAd == null) {
                    isRewardedInterLoading = true
                    RewardedInterstitialAd.load(
                        mContext,
                        rewardedIds,
                        AdRequest.Builder().build(),
                        object : RewardedInterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                Log.d(TAG, "AdmobRewardedInterstitial onAdFailedToLoad: ${loadAdError.message}")
                                isRewardedInterLoading = false
                                rewardedInterAd = null

                                listener.onAdFailedToLoad(loadAdError.toString())
                            }

                            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                                isRewardedInterLoading = false
                                rewardedInterAd = ad
                                Log.d(TAG, "AdmobRewardedInterstitial onAdLoaded: ")

                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d(TAG, "AdmobRewardedInterstitial onPreloaded: ")
                    listener.onPreloaded()
                }
            } else {
                Log.e(TAG, "AdmobRewardedInterstitial, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                listener.onAdFailedToLoad("isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    fun showRewardedInterstitialAd(
        activityContext: Activity,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        listener: RewardedOnShowCallBack
    ) {
        if (!isInternetConnected || isAppPurchased)
            return

        if (!isRewardedLoaded()) {
            Log.d(TAG, "AdmobRewardedInterstitial The rewarded ad wasn't ready yet.")
            return
        }

        rewardedInterAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d(TAG, "AdmobRewardedInterstitial admob Rewarded onAdClicked")
                listener.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "AdmobRewardedInterstitial admob Rewarded onAdDismissedFullScreenContent")
                listener.onAdDismissedFullScreenContent()
                rewardedInterAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e(
                    TAG,
                    "AdmobRewardedInterstitial admob Rewarded Interstitial onAdFailedToShowFullScreenContent: ${adError.message}"
                )
                listener.onAdFailedToShowFullScreenContent()
                rewardedInterAd = null
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(
                    TAG,
                    "AdmobRewardedInterstitial admob Rewarded Interstitial onAdShowedFullScreenContent"
                )
                listener.onAdShowedFullScreenContent()
                rewardedInterAd = null
            }

            override fun onAdImpression() {
                Log.d(TAG, "AdmobRewardedInterstitial admob Rewarded Interstitial onAdImpression")
                listener.onAdImpression()
            }
        }

        rewardedInterAd?.let { ad ->
            ad.show(activityContext) { rewardItem ->
                Log.d(TAG, "AdmobRewardedInterstitial admob Rewarded Interstitial onUserEarnedReward")
                listener.onUserEarnedReward()
            }

            ad.onPaidEventListener = OnPaidEventListener { adValue ->
                //ad revenue logic goes here
            }
        }
    }
}