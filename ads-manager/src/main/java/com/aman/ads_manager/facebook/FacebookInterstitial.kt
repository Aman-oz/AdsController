package com.aman.ads_manager.facebook

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

class FacebookInterstitial(private val activity: Activity) {
    private val TAG = FacebookInterstitial::class.java.simpleName

    private var interstitialAds : InterstitialAd? = null

    fun loadInterstitialAds(placementId:String){
        interstitialAds = InterstitialAd(activity,placementId)
        interstitialAds?.loadAd()
    }

    fun showInterstitialAds(afterSomeCode: () -> Unit) {
        if (interstitialAds != null && interstitialAds?.isAdLoaded == true){
            interstitialAds?.buildLoadAdConfig()?.withAdListener(object : InterstitialAdListener {
                override fun onError(ad: Ad?, adError: AdError?) {
                    Log.d(TAG, "onError: ${adError?.errorMessage}")
                }

                override fun onAdLoaded(ad: Ad?) {
                    Log.d(TAG,"onAdLoaded")
                }

                override fun onAdClicked(ad: Ad?) {
                    Log.d(TAG,"onAdClicked")
                }

                override fun onLoggingImpression(ad: Ad?) {
                    Log.d(TAG,"onLoggingImpression")
                }

                override fun onInterstitialDisplayed(ad: Ad?) {
                    Log.d(TAG,"onInterstitialDisplayed")
                }

                override fun onInterstitialDismissed(ad: Ad?) {
                    interstitialAds = null
                    afterSomeCode()
                }

            })?.build()
            interstitialAds?.show()
        }else{
            afterSomeCode()
        }

    }

}