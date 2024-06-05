package com.aman.ads_manager.facebook

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.aman.ads_manager.R
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdOptionsView
import com.facebook.ads.MediaView
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdBase
import com.facebook.ads.NativeAdLayout
import com.facebook.ads.NativeAdListener
import com.facebook.ads.NativeAdView
import com.facebook.ads.NativeAdViewAttributes

class FacebookNative(private val activity: Activity) {
    private val TAG = FacebookNative::class.java.simpleName

    fun loadNativeAd(
        customAdLayout: Int,
        nativeAdLayout: NativeAdLayout,
        isCustomLayout : Boolean,
        placementId: String,
    ){
        val nativeAd = NativeAd(activity,placementId)
        val nativeAdListener = object : NativeAdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                Log.d(TAG, "Facebook Native onError: ${adError?.errorMessage}")
            }

            override fun onAdLoaded(ad: Ad?) {
                if (isCustomLayout) {
                    customNativeLayout(customAdLayout,nativeAd, nativeAdLayout)
                }else{
                    val viewAttributes = NativeAdViewAttributes(activity)
                        .setBackgroundColor(Color.BLACK)
                        .setTitleTextColor(Color.WHITE)
                        .setDescriptionTextColor(Color.LTGRAY)
                        .setButtonColor(Color.WHITE)
                        .setButtonTextColor(Color.BLACK)

                    val adView = NativeAdView.render(activity, nativeAd, viewAttributes)
                    nativeAdLayout.addView(adView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800))
                }
            }

            override fun onAdClicked(ad: Ad?) {}
            override fun onLoggingImpression(ad: Ad?) {}
            override fun onMediaDownloaded(ad: Ad?) {}
        }
        nativeAd.loadAd(
            nativeAd.buildLoadAdConfig()
                .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                .withAdListener(nativeAdListener)
                .build()
        )
    }

    private fun customNativeLayout(customAdLayout: Int, nativeAd: NativeAd, nativeAdLayout: NativeAdLayout) {
        // Unregister last ad
        nativeAd.unregisterView()

        // Inflate the Ad view. The layout referenced should be the one you created in the last step.
        val adView = LayoutInflater.from(activity)
            .inflate(customAdLayout, nativeAdLayout, false)
        nativeAdLayout.addView(adView)

        // Add the AdOptionsView
        val adChoicesContainer = activity.findViewById<LinearLayout>(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(activity, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon = adView.findViewById<MediaView>(R.id.native_ad_icon)
        val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdMedia = adView.findViewById<MediaView>(R.id.native_ad_media)
        val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdBody = adView.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction = adView.findViewById<Button>(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews = ArrayList<View>()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)
        clickableViews.add(nativeAdIcon)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews
        )
    }


}