package com.aman.ads_manager.facebook

import android.app.Activity
import android.util.Log

import com.facebook.ads.*

class FacebookRewardedVideo(private val activity: Activity) {
    private val TAG = FacebookRewardedVideo::class.java.simpleName

    private var rewardedVideoAd : RewardedVideoAd? = null

    fun loadRewardedVideoAds(placementId:String){
        rewardedVideoAd = RewardedVideoAd(activity,placementId)
        rewardedVideoAd?.loadAd()
    }

    fun showRewardVideoAds(placementId: String,afterRewardedCode: () -> Unit){

        if (rewardedVideoAd != null && rewardedVideoAd?.isAdLoaded == true){
            rewardedVideoAd?.buildLoadAdConfig()?.withAdListener(object: RewardedVideoAdListener {
                override fun onError(ad: Ad?, adError: AdError?) {
                    Log.d(TAG, "onError: ${adError?.errorMessage}")
                }

                override fun onAdLoaded(ad: Ad?) {}

                override fun onAdClicked(ad: Ad?) {}

                override fun onLoggingImpression(ad: Ad?) {}

                override fun onRewardedVideoCompleted() {
                    rewardedVideoAd = null
                }

                override fun onRewardedVideoClosed() {
                    rewardedVideoAd = null
                    loadRewardedVideoAds(placementId)
                    afterRewardedCode()
                }

            })?.build()
            rewardedVideoAd?.show()
        }else{
            Log.d(TAG, "showRewardVideoAds: Ad is not Loaded")
        }
    }
}