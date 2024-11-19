package com.aman.ads_manager.admob.nativead

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aman.ads_manager.R
import com.aman.ads_manager.callbacks.BannerCallBack
import com.aman.ads_manager.constants.AdsConstants.preloadNativeAd
import com.aman.ads_manager.enums.NativeType
import com.aman.ads_manager.extensions.gone
import com.aman.ads_manager.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */
class AdmobNative {

    private val TAG = AdmobNative::class.java.simpleName
    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    private var adMobNativeAd: NativeAd? = null

    /**
     * load native ad and show
     */
    fun loadNativeAds(
        activity: Activity?,
        adLayout: Int,
        adFrame: FrameLayout,
        adShimmer: ShimmerFrameLayout,
        nativeId: String,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        nativeType: NativeType,
        bannerCallBack: BannerCallBack
    ) {
        val handlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "${throwable.message}")
            bannerCallBack.onAdFailedToLoad("${throwable.message}")
        }
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && !isAppPurchased && nativeId.isNotEmpty()) {
                    adFrame.visible()

                    // reuse of preloaded native ad
                    // if miss first native then use it next
                    if (preloadNativeAd != null) {
                        adMobNativeAd = preloadNativeAd
                        preloadNativeAd = null
                        Log.d(TAG, "admob native onAdLoaded")
                        bannerCallBack.onPreloaded()
                        displayNativeAd(mActivity,adLayout, adFrame, nativeType)
                        return
                    }
                    if (adMobNativeAd == null) {
                        CoroutineScope(Dispatchers.IO + handlerException).launch {
                            val builder: AdLoader.Builder = AdLoader.Builder(mActivity, nativeId)
                            val adLoader = builder.forNativeAd { unifiedNativeAd: NativeAd? ->

                                    adMobNativeAd = unifiedNativeAd
                                unifiedNativeAd?.setOnPaidEventListener { adValue ->
                                    bannerCallBack.onPaidEvent(adValue)


                                }

                                }.withAdListener(object : AdListener() {

                                        override fun onAdImpression() {
                                            super.onAdImpression()
                                            Log.d(TAG, "admob native onAdImpression")
                                            bannerCallBack.onAdImpression()
                                            adMobNativeAd = null
                                        }

                                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                            Log.e(TAG, "admob native onAdFailedToLoad: ${loadAdError.message}")
                                            bannerCallBack.onAdFailedToLoad(loadAdError.message)
                                            adFrame.gone()
                                            adShimmer.gone()
                                            adMobNativeAd = null
                                            super.onAdFailedToLoad(loadAdError)
                                        }

                                        override fun onAdLoaded() {
                                            super.onAdLoaded()

                                            adShimmer.gone()
                                            Log.d(TAG, "admob native onAdLoaded")
                                            bannerCallBack.onAdLoaded()
                                            displayNativeAd(mActivity, adLayout, adFrame, nativeType)

                                        }

                                    }).withNativeAdOptions(
                                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                                            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                                            .setRequestCustomMuteThisAd(true)
                                            .build()
                                    )
                                    .build()


                            adLoader.loadAd(AdRequest.Builder().build())

                        }
                    } else {
                        Log.e(TAG, "Native is already loaded")
                        bannerCallBack.onPreloaded()
                        displayNativeAd(mActivity, adLayout,adFrame, nativeType)
                    }

                } else {
                    adFrame.gone()
                    adShimmer.gone()
                    Log.e(TAG, "isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                    bannerCallBack.onAdFailedToLoad("isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                }

            } catch (ex: Exception) {
                Log.e(TAG, "${ex.message}")
                bannerCallBack.onAdFailedToLoad("${ex.message}")
            }
        }
    }

    private fun displayNativeAd(
        activity: Activity?,
        adLayout: Int,
        adMobNativeContainer: FrameLayout,
        nativeType: NativeType,
    ) {
        activity?.let { mActivity ->
            try {
                adMobNativeAd?.let { ad ->
                    val inflater = LayoutInflater.from(mActivity)

                    val adView: NativeAdView = inflater.inflate(adLayout, null) as NativeAdView
                        /*when (nativeType) {
                        NativeType.BANNER -> inflater.inflate(R.layout.native_banner, null) as NativeAdView
                        NativeType.SMALL -> inflater.inflate(R.layout.native_small, null) as NativeAdView
                        NativeType.LARGE -> inflater.inflate(R.layout.native_large, null) as NativeAdView
                        NativeType.LARGE_ADJUSTED -> if (isSupportFullScreen(mActivity)) {
                            inflater.inflate(R.layout.native_large, null) as NativeAdView
                        } else {
                            inflater.inflate(R.layout.native_small, null) as NativeAdView
                        }
                    }*/

                    /******ad revenue logic*******/
                    ad.setOnPaidEventListener { adValue ->
                        //implement logic for revenue
                    }


                    val viewGroup: ViewGroup? = adView.parent as ViewGroup?
                    viewGroup?.removeView(adView)

                    adMobNativeContainer.removeAllViews()
                    adMobNativeContainer.addView(adView)

                    if (nativeType == NativeType.LARGE) {
                        val mediaView: MediaView = adView.findViewById(R.id.media_view)
                        adView.mediaView = mediaView
                    }
                    if (nativeType == NativeType.LARGE_ADJUSTED) {
                        if (isSupportFullScreen(mActivity)) {
                            val mediaView: MediaView = adView.findViewById(R.id.media_view)
                            adView.mediaView = mediaView
                        }
                    }

                    // Set other ad assets.
                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_app_icon)

                    //Headline
                    adView.headlineView?.let { headline ->
                        (headline as TextView).text = ad.headline
                        headline.isSelected = true
                    }

                    //Body
                    adView.bodyView?.let { bodyView ->
                        if (ad.body == null) {
                            bodyView.visibility = View.INVISIBLE
                        } else {
                            bodyView.visibility = View.VISIBLE
                            (bodyView as TextView).text = ad.body
                        }
                    }

                    //Call to Action
                    adView.callToActionView?.let { ctaView ->
                        if (ad.callToAction == null) {
                            ctaView.visibility = View.GONE
                        } else {
                            ctaView.visibility = View.VISIBLE
                            (ctaView as Button).text = ad.callToAction
                        }
                    }

                    //Icon
                    adView.iconView?.let { iconView ->
                        if (ad.icon == null) {
                            iconView.visibility = View.GONE
                        } else {
                            (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                            iconView.visibility = View.VISIBLE
                        }
                    }

                    adView.advertiserView?.let { adverView ->
                        if (ad.advertiser == null) {
                            adverView.visibility = View.GONE
                        } else {
                            (adverView as TextView).text = ad.advertiser
                            adverView.visibility = View.GONE
                        }
                    }

                    adView.setNativeAd(ad)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "displayNativeAd: ${ex.message}")
            }
        }
    }

    private fun isSupportFullScreen(activity: Activity): Boolean {
        try {
            val outMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
            if (outMetrics.heightPixels > 1280) {
                return true
            }
        } catch (ignored: Exception) {
        }
        return false
    }
}