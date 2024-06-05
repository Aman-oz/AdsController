package com.aman.adscontroller.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.ads_manager.facebook.FacebookNative
import com.aman.ads_manager.facebook.FacebookNativeBanner
import com.aman.adscontroller.R
import com.aman.adscontroller.databinding.ActivityNativeAdsctivityBinding
import com.facebook.ads.NativeBannerAdView

class NativeAdsActivity : AppCompatActivity() {
    private val binding: ActivityNativeAdsctivityBinding by lazy {
        ActivityNativeAdsctivityBinding.inflate(layoutInflater)
    }
    private lateinit var fbNativeAds: FacebookNative
    private lateinit var fbNativeBannerAds: FacebookNativeBanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fbNativeAds = FacebookNative(this)
        fbNativeBannerAds = FacebookNativeBanner(this)

        fbNativeAds.loadNativeAd(
            R.layout.facebook_native_ad_container,
            binding.nativeAdLayout,
            true,
            "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
        )

        fbNativeBannerAds.loadNativeBannerAds(
            R.layout.facebook_native_banner_ad_container,
            binding.nativeBannerAdLayout,
            NativeBannerAdView.Type.HEIGHT_120,
            true,
            "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
        )

    }
}