package com.aman.adscontroller.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.ads_manager.facebook.FacebookInterstitial
import com.aman.ads_manager.facebook.FacebookRewardedInterstitial
import com.aman.ads_manager.facebook.FacebookRewardedVideo
import com.aman.adscontroller.R
import com.aman.adscontroller.databinding.ActivityFacebookAdsBinding

class FacebookAdsActivity : AppCompatActivity() {
    private val TAG = FacebookAdsActivity::class.java.simpleName
    private val binding: ActivityFacebookAdsBinding by lazy {
        ActivityFacebookAdsBinding.inflate(layoutInflater)
    }

    private lateinit var facebookInterAd: FacebookInterstitial
    private lateinit var facebookRewardedVideoAd: FacebookRewardedVideo
    private lateinit var facebookRewardedInterstitialAd: FacebookRewardedInterstitial
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        facebookInterAd = FacebookInterstitial(this)
        facebookInterAd.loadInterstitialAds("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")

        facebookRewardedVideoAd = FacebookRewardedVideo(this)
        facebookRewardedVideoAd.loadRewardedVideoAds("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")

        facebookRewardedInterstitialAd = FacebookRewardedInterstitial(this)
        facebookRewardedInterstitialAd.loadRewardedInterstitialAds("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")

        binding.btnNativeAd.setOnClickListener {
            startActivity(Intent(this, NativeAdsActivity::class.java))
        }

        binding.btnInterstitialAd.setOnClickListener {
            facebookInterAd.showInterstitialAds {
                startActivity(Intent(this, NativeAdsActivity::class.java))
            }
        }

        binding.btnRewardedVideoAd.setOnClickListener {
            facebookRewardedVideoAd.showRewardVideoAds("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID") {
                startActivity(Intent(this, NativeAdsActivity::class.java))
            }
        }

        binding.btnRewardedInterstitialAd.setOnClickListener {
            facebookRewardedInterstitialAd.showRewardedInterstitialAds("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID") {
                startActivity(Intent(this, NativeAdsActivity::class.java))
            }
        }
    }
}