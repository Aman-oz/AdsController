package com.aman.ads_manager.facebook

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.RewardedInterstitialAd
import com.facebook.ads.RewardedInterstitialAdListener

class FacebookRewardedInterstitial(private val activity: Activity) {
    private val TAG = FacebookRewardedInterstitial::class.java.simpleName

    private var rewardedInterstitialAd : RewardedInterstitialAd? = null


    fun loadRewardedInterstitialAds(placementId : String){
        rewardedInterstitialAd = RewardedInterstitialAd(activity,placementId)
        rewardedInterstitialAd?.loadAd()
    }

    fun showRewardedInterstitialAds(placementId: String, afterRewardedCoin : () -> Unit ){
        if (rewardedInterstitialAd != null && rewardedInterstitialAd?.isAdLoaded == true){
            rewardedInterstitialAd?.buildLoadAdConfig()?.withAdListener(object :
                RewardedInterstitialAdListener {
                override fun onError(ad: Ad?, adError: AdError?) {
                    Log.d(TAG, "onError: ${adError?.errorMessage}")
                }

                override fun onAdLoaded(ad: Ad?) {}

                override fun onAdClicked(ad: Ad?) {}

                override fun onLoggingImpression(ad: Ad?) {}

                override fun onRewardedInterstitialCompleted() {
                    rewardedInterstitialAd = null
                }

                override fun onRewardedInterstitialClosed() {
                    rewardedInterstitialAd = null
                    loadRewardedInterstitialAds(placementId)
                    afterRewardedCoin()
                }

            })?.build()
            rewardedInterstitialAd?.show()
        }else{
            Log.d(TAG, "showRewardedInterstitialAds: Ad is not Loaded")
        }
    }
}