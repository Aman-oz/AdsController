package com.aman.ads_manager.admob.banner

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.aman.ads_manager.callbacks.BannerCallBack
import com.aman.ads_manager.enums.BannerType
import com.aman.ads_manager.extensions.gone
import com.aman.ads_manager.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener

/**
 * @Author: Aman Ullah
 * @Date: 06,May,2024.
 * Software Engineer Android
 */
class AdmobBanner {
    private val TAG = AdmobBanner::class.java.simpleName

    private var adaptiveAdView: AdView? = null

    fun loadBannerAds(
        activity: Activity?,
        adFrame: FrameLayout,
        adShimmer: ShimmerFrameLayout,
        adRoot: View,
        bannerId: String,
        adEnable: Boolean = true,
        isAppPurchased: Boolean = false,
        isInternetConnected: Boolean,
        bannerType: BannerType = BannerType.ADAPTIVE_BANNER,
        bannerCallBack: BannerCallBack
    ) {
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && adEnable && !isAppPurchased && bannerId.isNotEmpty()) {
                    adFrame.visible()
                    adShimmer.visible()
                    adaptiveAdView = AdView(mActivity)
                    adaptiveAdView?.adUnitId = bannerId
                    try {
                        adaptiveAdView?.setAdSize(getAdSize(mActivity, adFrame))
                    }catch (ex:Exception){
                        adaptiveAdView?.setAdSize(AdSize.BANNER)
                    }

                    val adRequest: AdRequest =   when(adEnable){
                        true -> when (bannerType) {

                            BannerType.ADAPTIVE_BANNER -> {

                                Log.d(TAG, "loadBannerAds adaptive: $bannerType")
                                AdRequest.Builder().build()
                            }
                            BannerType.COLLAPSIBLE_BOTTOM -> {
                                Log.d(TAG, "loadBannerAds cbb: $bannerType")
                                AdRequest
                                    .Builder()
                                    .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                                        putString("collapsible", "bottom")
                                    })
                                    .build()
                            }
                            BannerType.COLLAPSIBLE_TOP -> {

                                Log.d(TAG, "loadBannerAds cbt: $bannerType")
                                AdRequest
                                    .Builder()
                                    .addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                                        putString("collapsible", "top")
                                    })
                                    .build()
                            }

                            BannerType.BANNER -> {

                                Log.d(TAG, "loadBannerAds banner: $bannerType")
                                AdRequest.Builder()
                                    .build()
                            }
                        }
                        else -> {

                            Log.d(TAG, "loadBannerAds else: $bannerType")
                            AdRequest
                                .Builder()
                                .build()
                        }
                    }

                    adaptiveAdView?.loadAd(adRequest)

                    adaptiveAdView?.adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            Log.d("AdsInformation", "admob banner onAdLoaded")
                            adShimmer.gone()
                            displayBannerAd(adFrame, adShimmer, adRoot)
                            bannerCallBack.onAdLoaded()
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e("AdsInformation", "admob banner onAdFailedToLoad: ${adError.message}")
                            adFrame.gone()
                            adRoot.gone()
                            adShimmer.gone()
                            bannerCallBack.onAdFailedToLoad(adError.message)
                        }

                        override fun onAdImpression() {
                            Log.d("AdsInformation", "admob banner onAdImpression")
                            bannerCallBack.onAdImpression()
                            super.onAdImpression()
                        }

                        override fun onAdClicked() {
                            Log.d("AdsInformation", "admob banner onAdClicked")
                            bannerCallBack.onAdClicked()
                            super.onAdClicked()
                        }

                        override fun onAdClosed() {
                            Log.d("AdsInformation", "admob banner onAdClosed")
                            bannerCallBack.onAdClosed()
                            super.onAdClosed()
                        }

                        override fun onAdOpened() {
                            Log.d("AdsInformation", "admob banner onAdOpened")
                            bannerCallBack.onAdOpened()
                            super.onAdOpened()
                        }
                    }

                    adaptiveAdView?.onPaidEventListener = OnPaidEventListener { adValue ->
                        //implement ad Revenue logic
                    }
                } else {
                    adFrame.removeAllViews()
                    adFrame.gone()
                    adRoot.gone()
                    adShimmer.gone()
                    Log.e("AdsInformation", "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                    bannerCallBack.onAdFailedToLoad("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                }
            } catch (ex: Exception) {
                Log.e("AdsInformation", "${ex.message}")
                bannerCallBack.onAdFailedToLoad("${ex.message}")
            }
        }

    }

    private fun displayBannerAd(adFrame: FrameLayout, adShimmer: ShimmerFrameLayout, adRoot: View) {
        try {
            if (adaptiveAdView != null) {
                val viewGroup: ViewGroup? = adaptiveAdView?.parent as? ViewGroup?
                viewGroup?.removeView(adaptiveAdView)

                adFrame.removeAllViews()
                adFrame.addView(adaptiveAdView)
            } else {
                adFrame.removeAllViews()
                adFrame.gone()
                adRoot.gone()
                adShimmer.gone()
            }
        } catch (ex: Exception) {
            Log.e("AdsInformation", "inflateBannerAd: ${ex.message}")
        }

    }

    fun bannerOnPause() {
        try {
            adaptiveAdView?.pause()
        } catch (ex: Exception) {
            Log.e("AdsInformation", "bannerOnPause: ${ex.message}")
        }

    }

    fun bannerOnResume() {
        try {
            adaptiveAdView?.resume()
        } catch (ex: Exception) {
            Log.e("AdsInformation", "bannerOnPause: ${ex.message}")
        }
    }

    fun bannerOnDestroy() {
        try {
            adaptiveAdView?.destroy()
            adaptiveAdView = null
        } catch (ex: Exception) {
            Log.e("AdsInformation", "bannerOnPause: ${ex.message}")
        }
    }

    @Suppress("DEPRECATION")
    @Throws(Exception::class)
    private fun getAdSize(mActivity: Activity, adContainer: FrameLayout): AdSize {
        val display = mActivity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
    }

    private fun getAdSize(activity: Activity): AdSize {
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        val adWidth = (widthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

}