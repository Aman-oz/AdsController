1. Adding AdsController to your project
Include jitpack in your root settings.gradle file.
pluginManagement {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

And add it's dependency to your app level build.gradle file:
dependencies {
    implementation 'com.github.Aman-oz:AdsController:latest_version'
}

Sync your project, and 😱 boom 🔥 you have added AdsController successfully. ❗

2. Usage
First declare appOpen manager and current activity in your application class:
   private lateinit var appOpenAdManager: OpenAdManager
   private var currentActivity: Activity? = null

Initialize the app open manager with two arguments in the oncreate of your application class:
 		appOpenAdManager = OpenAdManager(this, AD_UNIT_ID)

Attach lifecycle observer to your application class:
				registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

			 //initialize mobile ads.
        MobileAds.initialize(this)

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("566E9CF2E5BF0F3F112621BA0CDD12B1", "25E8003CF5DD39E8AF2DE715AC192AF4")).build()
        )

/** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let {
            showAdIfAvailable(it, object : OnShowAdCompleteListener {
            override fun onShowAdComplete() {
                //cODO("Not yet implemented")
            }

        })
        }
    }

override all the activit lifecycle methods in the app :
override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

	
Method for showing app open ad
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        if (!MainActivity.isAdShowing)
            appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

	Method for loading app open ad:

    fun loadAd(activity: Activity) {
        appOpenAdManager.loadAd(activity)
    }

Calling app open add in your splash screen if add is available:
(application as AppClass).showAdIfAvailable(
                            this@SplashActivity,
                            object : OnShowAdCompleteListener {
                                override fun onShowAdComplete() {
                                    // Check if the consent form is currently on screen before moving to the main
                                    if (googleMobileAdsConsentManager.canRequestAds) {
                                        startMainActivity()
                                    }
                                }
                            }
                        )

For checking the ads consents implement consent in your splash screen:
		private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager

		onCreate() {
			googleMobileAdsConsentManager =
                GoogleMobileAdsConsentManager.getInstance(applicationContext)
            googleMobileAdsConsentManager.gatherConsent(this) { consentError ->
                if (consentError != null) {
                    // Consent not obtained in current session.
                    Log.w(
                        TAG,
                        String.format("%s: %s", consentError.errorCode, consentError.message)
                    )
                }

                if (googleMobileAdsConsentManager.canRequestAds) {
                    initializeMobileAdsSdk()
                }

                if (secondsRemaining <= 0) {
                    startMainActivity()
                }
            }
}

now Check consents:
if (googleMobileAdsConsentManager.canRequestAds) {
                initializeMobileAdsSdk()
            }

private fun initializeMobileAdsSdk() {
            if (isMobileAdsInitializeCalled.getAndSet(true)) {
                return
            }

            // Initialize the Mobile Ads SDK.
            MobileAds.initialize(this) {}

            // Load an ad.
            (application as AppClass).loadAd(this)
        }
******************Now everything is ready lets load and show ads in your activity*******************
1. Create instances for the ads accordingly
 
   private lateinit var bannerAd: AdmobBanner
    private lateinit var nativeAd: AdmobNative
    private lateinit var nativeAdPreLoad: AdmobNativePreload
    private lateinit var interAd: AdmobInterstitial
    private lateinit var rewardedAd: AdmobRewarded
    private lateinit var rewardedInterAd: AdmobRewardedInterstitial

2. Initialize it:
	 nativeAd = AdmobNative()
   nativeAdPreLoad = AdmobNativePreload()
   rewardedAd = AdmobRewarded()
   rewardedInterAd = AdmobRewardedInterstitial()
   interAd = AdmobInterstitial()

3. Fro interstitial and rewarded ads you also need to call the initialize() method of these classes:
	interAd.initialize(this)
	rewardedAd.initialize(this)
	rewardedInterAd.initialize(this)

Banner Ad(note pass the type of the banner using BannerType class:
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

            }
        )

	Native Banner:
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

            }
        )

Native Small(without media):
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

            }
        )

Native Large(with media):
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

            }
        )

LoadInterstitial:
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

Show Interstitial:
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

        })
    }


Load Rewarded:
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

Show Rewarded:
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

Load Rewarded Interstitial:
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

Show Rewarded Interstitial:
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


📌 Please, feel free to give me a star 🌟, I also love sparkles ✨ ☺️
