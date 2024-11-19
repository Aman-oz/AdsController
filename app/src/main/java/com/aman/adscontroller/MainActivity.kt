package com.aman.adscontroller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.ads_manager.admob.banner.AdmobBanner
import com.aman.ads_manager.admob.interstitital.AdmobInterstitial
import com.aman.ads_manager.admob.nativead.AdmobNative
import com.aman.ads_manager.admob.nativead.AdmobNativePreload
import com.aman.ads_manager.admob.rewarded.AdmobRewarded
import com.aman.ads_manager.admob.rewarded.AdmobRewardedInterstitial
import com.aman.ads_manager.callbacks.BannerCallBack
import com.aman.ads_manager.callbacks.InterstitialOnLoadCallBack
import com.aman.ads_manager.callbacks.InterstitialOnShowCallBack
import com.aman.ads_manager.callbacks.RewardedOnLoadCallBack
import com.aman.ads_manager.callbacks.RewardedOnShowCallBack
import com.aman.ads_manager.enums.BannerType
import com.aman.ads_manager.enums.NativeType
import com.aman.ads_manager.managers.InternetManager
import com.aman.ads_manager.utils.CleanMemory
import com.aman.adscontroller.databinding.ActivityMainBinding
import com.aman.adscontroller.listeners.RapidSafeListener.setOnRapidClickSafeListener
import com.aman.adscontroller.ui.FacebookAdsActivity
import com.aman.adscontroller.ui.SecondActivity
import com.google.android.gms.ads.AdValue

/**
 * @Author: Aman Ullah
 * @Date: 07,May,2024.
 * Software Engineer Android
 */

const val REWARDED_VIDEO = "/6499/example/rewarded-video"
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var nativeAd: AdmobNative
    private lateinit var nativeAdPreLoad: AdmobNativePreload
    private lateinit var interAd: AdmobInterstitial
    private lateinit var rewardedAd: AdmobRewarded
    private lateinit var rewardedInterAd: AdmobRewardedInterstitial

    private val bannerAd by lazy { AdmobBanner() }
    private val connectivityManager by lazy { InternetManager(this) }

    companion object {
        var isAdShowing = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        nativeAd = AdmobNative()
        nativeAdPreLoad = AdmobNativePreload()
        rewardedAd = AdmobRewarded(this)
        rewardedInterAd = AdmobRewardedInterstitial(this)
        interAd = AdmobInterstitial(this)

        /*interAd.initialize(this)
        rewardedAd.initialize(this)
        rewardedInterAd.initialize(this)*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnNext.setOnRapidClickSafeListener {
            showInterstitialAd()
            //openNextActivity()
        }

        binding.btnShowRewardedAd.setOnRapidClickSafeListener {
            showRewardedAd()
        }

        binding.btnShowRewardedInterAd.setOnRapidClickSafeListener {
            showRewardedInterstitialAd()
        }

        binding.btnPreLoadNativeAd.setOnClickListener {
            preLoadNativeForNextScreen()
        }

        binding.btnFacebookAds.setOnClickListener {
            startActivity(Intent(this, FacebookAdsActivity::class.java))
        }

        bannerAd.loadBannerAds(
            this,
            binding.adFrame,
            binding.adShimmer,
            binding.adRootLayout,
            bannerId = "ca-app-pub-3940256099942544/6300978111",
            adEnable = true,
            isAppPurchased = false,
            isInternetConnected = true,
            bannerType = BannerType.BANNER,
            bannerCallBack = object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "onAdClosed: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "onAdOpened: ")
                }

                override fun onPaidEvent(adValue: AdValue) {

                    Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
                }

            }
        )

        nativeAd.loadNativeAds(
            this,
            R.layout.native_ad_banner_layout,
            binding.customNativeBannerAdLayout.adFrame,
            binding.customNativeBannerAdLayout.shimmer,
            "ca-app-pub-3940256099942544/2247696110",
            false,
            connectivityManager.isInternetConnected,
            NativeType.BANNER,
            bannerCallBack = object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "onAdClosed: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "onAdOpened: ")
                }

                override fun onPaidEvent(adValue: AdValue) {
                    Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
                }

            }
        )

        nativeAd.loadNativeAds(
            this,
            R.layout.native_ad_small_without_media,
            binding.customNativeSmallAdLayout.adFrame,
            binding.customNativeSmallAdLayout.shimmer,
            "ca-app-pub-3940256099942544/2247696110",
            false,
            connectivityManager.isInternetConnected,
            NativeType.SMALL,
            bannerCallBack = object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "onAdClosed: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "onAdOpened: ")
                }

                override fun onPaidEvent(adValue: AdValue) {

                    Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
                }

            }
        )

        nativeAd.loadNativeAds(
            this,
            R.layout.native_ad_layout_with_media,
            binding.customNativeLargeAdLayout.adFrame,
            binding.customNativeLargeAdLayout.shimmer,
            "ca-app-pub-3940256099942544/2247696110",
            false,
            connectivityManager.isInternetConnected,
            NativeType.LARGE,
            bannerCallBack = object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "onAdClosed: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "onAdOpened: ")
                }

                override fun onPaidEvent(adValue: AdValue) {

                    Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
                }

            }
        )

        loadInterstitialAd()
        loadRewardedAd()
        loadRewardedInterstitialAd()
    }

    //Preload function
    private fun preLoadNativeForNextScreen() {
        nativeAdPreLoad.loadNativeAds(
            this,
            "ca-app-pub-3940256099942544/2247696110",
            false,
            connectivityManager.isInternetConnected,
            bannerCallBack = object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                    openNextActivity()
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                    openNextActivity()
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                    openNextActivity()
                }

                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "onAdClosed: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "onAdOpened: ")
                }

                override fun onPaidEvent(adValue: AdValue) {

                    Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
                }

            }
        )
    }

    private fun loadInterstitialAd() {
        interAd.loadInterstitialAd(
            "ca-app-pub-3940256099942544/1033173712",
            false,
            connectivityManager.isInternetConnected,
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    Log.d(TAG, "onAdFailedToLoad: ")
                }

                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onPreloaded() {
                    Log.d(TAG, "onPreloaded: ")
                }

            }
        )
    }

    private fun showInterstitialAd() {
        interAd.showInterstitialAd(this, object : InterstitialOnShowCallBack {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "onAdDismissedFullScreenContent: ")
                isAdShowing = false
                interAd.loadAgainInterstitialAd("ca-app-pub-3940256099942544/1033173712")
            }

            override fun onAdFailedToShowFullScreenContent() {
                isAdShowing = false
                Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
            }

            override fun onAdShowedFullScreenContent() {
                isAdShowing = true
                Log.d(TAG, "onAdShowedFullScreenContent: ")
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression: ")
            }

            override fun onAdNull() {
                isAdShowing = false
                interAd.loadAgainInterstitialAd("ca-app-pub-3940256099942544/1033173712")
            }

            override fun onPaidEvent(adValue: AdValue) {

                Log.d(TAG, "onPaidEvent: adValue: ${adValue.valueMicros}")
            }

        })
    }

    private fun showRewardedAd() {
        if (rewardedAd.isRewardedLoaded()) {
            rewardedAd.showRewardedAd(this, object : RewardedOnShowCallBack {
                override fun onAdClicked() {
                    Log.d(TAG, "onAdClicked: ")
                }

                override fun onAdDismissedFullScreenContent() {
                    isAdShowing = false
                    Log.d(TAG, "onAdDismissedFullScreenContent: ")
                }

                override fun onAdFailedToShowFullScreenContent() {
                    isAdShowing = false
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onAdShowedFullScreenContent() {
                    isAdShowing = true
                    Log.d(TAG, "onAdShowedFullScreenContent: ")
                }

                override fun onUserEarnedReward() {
                    Log.d(TAG, "onUserEarnedReward: ")
                }

            })
        }
    }

    private fun loadRewardedAd() {
        if (!rewardedAd.isRewardedLoaded()) {
            rewardedAd.loadRewardedAd(
                "ca-app-pub-3940256099942544/5224354917",
                1,
                false,
                connectivityManager.isInternetConnected,
                object : RewardedOnLoadCallBack {
                    override fun onAdFailedToLoad(adError: String) {
                        Log.d(TAG, "onAdFailedToLoad: ")
                    }

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded: ")
                    }

                    override fun onPreloaded() {
                        Log.d(
                            TAG,
                            "onPreloaded: "
                        )
                    }

                }
            )
        }
    }

    private fun loadRewardedInterstitialAd() {
        if (!rewardedInterAd.isRewardedLoaded()) {
            rewardedInterAd.loadRewardedInterstitialAd(
                REWARDED_VIDEO,
                false,
                connectivityManager.isInternetConnected,
                object : RewardedOnLoadCallBack {
                    override fun onAdFailedToLoad(adError: String) {
                        Log.d(TAG, "loadRewardedInterstitialAd onAdFailedToLoad: $adError")
                    }

                    override fun onAdLoaded() {
                        Log.d(TAG, "loadRewardedInterstitialAd onAdLoaded: ")
                    }

                    override fun onPreloaded() {
                        Log.d(TAG, "loadRewardedInterstitialAd onPreloaded: ")
                    }

                }
            )
        }
    }

    private fun showRewardedInterstitialAd() {

        if (rewardedInterAd.isRewardedLoaded()) {
            rewardedInterAd.showRewardedInterstitialAd(
                this,
                false,
                connectivityManager.isInternetConnected,
                object : RewardedOnShowCallBack {
                    override fun onAdClicked() {
                        Log.d(TAG, "showRewardedInterstitialAd onAdClicked: ")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        isAdShowing = false
                        Log.d(TAG, "showRewardedInterstitialAd onAdDismissedFullScreenContent: ")
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        isAdShowing = false
                        Log.d(TAG, "showRewardedInterstitialAd onAdFailedToShowFullScreenContent: ")
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "showRewardedInterstitialAd onAdImpression: ")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isAdShowing = true
                        Log.d(TAG, "showRewardedInterstitialAd onAdShowedFullScreenContent: ")
                    }

                    override fun onUserEarnedReward() {
                        Log.d(TAG, "showRewardedInterstitialAd onUserEarnedReward: ")
                    }

                }
            )
        }
    }

    private fun openNextActivity() {
        startActivity(Intent(this,SecondActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        CleanMemory.clean()
        isAdShowing = false
        super.onDestroy()
    }
}