package com.aman.ads_manager.admob.interstitital

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.aman.ads_manager.callbacks.InterstitialOnLoadCallBack
import com.aman.ads_manager.callbacks.InterstitialOnShowCallBack
import com.aman.ads_manager.constants.AdsConstants
import com.aman.ads_manager.constants.AdsConstants.isInterLoading
import com.aman.ads_manager.constants.AdsConstants.mInterstitialAd
import com.aman.ads_manager.utils.SharedPreferencesHelper
import com.aman.ads_manager.utils.SharedPreferencesHelper.IS_INTERSTITIAL_AD_SHOWING
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
class AdmobInterstitial {

    private val TAG = AdmobInterstitial::class.java.simpleName

    private var context: Context? = null

    companion object {
        var isInterstitialShowing = false
    }

    fun initialize(context: Context) {
        this.context = context
    }

    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    fun loadInterstitialAd(
        interId: String,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        listener: InterstitialOnLoadCallBack
    ) {
        context?.let { mContext ->
            if (isInternetConnected && !isAppPurchased && !isInterLoading && interId.isNotEmpty()) {
                if (mInterstitialAd == null) {
                    isInterLoading = true
                    InterstitialAd.load(
                        mContext,
                        interId,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.e(TAG, "admob Interstitial onAdFailedToLoad: ${adError.message}")
                                isInterLoading = false
                                mInterstitialAd = null
                                listener.onAdFailedToLoad(adError.toString())
                            }

                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                Log.d(TAG, "admob Interstitial onAdLoaded")
                                isInterLoading = false
                                mInterstitialAd = interstitialAd
                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d(TAG, "admob Interstitial onPreloaded")
                    listener.onPreloaded()
                }

            } else {
                Log.e(TAG, "isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                listener.onAdFailedToLoad("isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    fun showInterstitialAd(activity: Activity?, listener: InterstitialOnShowCallBack) {
        activity?.let { mActivity ->
            if (mInterstitialAd != null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "admob Interstitial onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()

                        isInterstitialShowing = false

                        mInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "admob Interstitial onAdFailedToShowFullScreenContent: ${adError.message}")
                        listener.onAdFailedToShowFullScreenContent()

                        isInterstitialShowing = false

                        mInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "admob Interstitial onAdShowedFullScreenContent")
                        listener.onAdShowedFullScreenContent()

                        isInterstitialShowing = true

                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "admob Interstitial onAdImpression")
                        listener.onAdImpression()
                    }
                }
                isInterstitialShowing = true
                mInterstitialAd?.show(mActivity)
            }else {
                listener.onAdNull()
            }
        }
    }

    fun showAndLoadInterstitialAd(activity: Activity?, interId: String, listener: InterstitialOnShowCallBack) {
        Log.d(TAG, "showAndLoadInterstitialAd: *************************")
        activity?.let { mActivity ->
            if (mInterstitialAd != null && interId.isNotEmpty()) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        isInterstitialShowing = false
                        Log.d(TAG, "admob Interstitial onAdDismissedFullScreenContent, isShowing: $isInterstitialShowing")
                        listener.onAdDismissedFullScreenContent()


                        loadAgainInterstitialAd(interId)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "admob Interstitial onAdFailedToShowFullScreenContent: ${adError.message}")
                        listener.onAdFailedToShowFullScreenContent()

                        isInterstitialShowing = true

                        mInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        listener.onAdShowedFullScreenContent()

                        isInterstitialShowing = true
                        Log.d(TAG, "admob Interstitial onAdShowedFullScreenContent: isShowing: $isInterstitialShowing")

                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "admob Interstitial onAdImpression")
                        listener.onAdImpression()
                    }
                }
                isInterstitialShowing = true
                mInterstitialAd?.show(mActivity)
            }
        }
    }

    fun loadAgainInterstitialAd(
        interId: String
    ) {
        context?.let { mContext ->
            if (mInterstitialAd == null && !isInterLoading) {
                isInterLoading = true
                InterstitialAd.load(
                    mContext,
                    interId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(TAG, "admob Interstitial onAdFailedToLoad: $adError")
                            isInterLoading = false
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(TAG, "admob Interstitial onAdLoaded")
                            isInterLoading = false
                            mInterstitialAd = interstitialAd

                        }
                    })
            }else {
                Log.d(TAG, "loadAgainInterstitialAd: else")
            }
        }
    }

    fun isInterstitialLoaded(): Boolean {
        return mInterstitialAd != null
    }

    fun dismissInterstitial() {
        mInterstitialAd = null
    }

}