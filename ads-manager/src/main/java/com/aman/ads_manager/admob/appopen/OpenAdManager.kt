package com.aman.ads_manager.admob.appopen

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.aman.ads_manager.admob.interstitital.AdmobInterstitial
import com.aman.ads_manager.admob.rewarded.AdmobRewarded
import com.aman.ads_manager.callbacks.OnShowAdCompleteListener
import com.aman.ads_manager.concent.GoogleMobileAdsConsentManager
import com.aman.ads_manager.constants.AdsConstants
import com.aman.ads_manager.utils.SharedPreferencesHelper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

private const val AD_UNIT_ID = "/6499/example/app-open"

class OpenAdManager(private val application: Application, private val AD_UNIT_ID: String) {

    private val TAG = OpenAdManager::class.java.simpleName

    private val googleMobileAdsConsentManager =
        GoogleMobileAdsConsentManager.getInstance(application.applicationContext)
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false

    private var loadTime: Long = 0

    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdManagerAdRequest.Builder().build()
        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {

                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    Log.d(TAG, "onAdLoaded.")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                    Log.d(TAG, "onAdFailedToLoad: " + loadAdError.message)
                }
            }
        )
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        if (isShowingAd) {
            Log.d(TAG, "The app open ad is already showing.")
            return
        }

        if (AdsConstants.isInterstitialAdShowing) {
            Log.d(TAG, "The interstitial ad is already showing.")
            return
        }
        if (AdmobInterstitial.isInterstitialShowing || AdmobRewarded.isRewardedShowing) {
            Log.d(TAG, "one ad is currently showing, skipping open ad.")
            onShowAdCompleteListener.onShowAdComplete()
            return
        }

        if (!isAdAvailable()) {
            Log.d(TAG, "The app open ad is not ready yet.")
            onShowAdCompleteListener.onShowAdComplete()
            if (googleMobileAdsConsentManager.canRequestAds) {
                loadAd(activity)
            }
            return
        }

        Log.d(TAG, "Will show ad.")

        appOpenAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {

                    appOpenAd = null
                    isShowingAd = false
                    Log.d(TAG, "onAdDismissedFullScreenContent.")

                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.message)

                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "onAdShowedFullScreenContent.")
                }
            }
        isShowingAd = true
        appOpenAd?.show(activity)
    }

}