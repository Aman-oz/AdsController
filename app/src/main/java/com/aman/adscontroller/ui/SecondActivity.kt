package com.aman.adscontroller.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.ads_manager.admob.nativead.AdmobNative
import com.aman.ads_manager.admob.nativead.AdmobNativePreload
import com.aman.ads_manager.callbacks.BannerCallBack
import com.aman.ads_manager.enums.NativeType
import com.aman.adscontroller.R
import com.aman.adscontroller.databinding.ActivitySecondBinding

const val NATIVE_AD_ID = "/6499/example/native"
class SecondActivity : AppCompatActivity() {
    private val TAG = SecondActivity::class.java.simpleName

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    private lateinit var admobNative: AdmobNativePreload
    private lateinit var nativeAd: AdmobNative

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        admobNative = AdmobNativePreload()
        nativeAd = AdmobNative()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        admobNative.showNativeAds(this,
            R.layout.native_ad_layout_with_media,
            binding.adFrameNativeLarge,
            NativeType.LARGE
        )

        loadMyCustomNative()


    }

    private fun loadMyCustomNative() {
        nativeAd.loadNativeAds(
            this,
            R.layout.native_ad_layout_with_media,
            binding.customAdLayoutInclude.adFrame,
            binding.customAdLayoutInclude.shimmer,
            NATIVE_AD_ID,
            1,
            false,
            true,
            NativeType.LARGE,
            object : BannerCallBack {
                override fun onAdClicked() {
                    //ODO("Not yet implemented")
                }

                override fun onAdClosed() {
                    //ODO("Not yet implemented")
                }

                override fun onAdFailedToLoad(adError: String) {
                    //ODO("Not yet implemented")
                }

                override fun onAdImpression() {
                    //ODO("Not yet implemented")
                }

                override fun onAdLoaded() {
                    //ODO("Not yet implemented")
                }

                override fun onAdOpened() {
                    //ODO("Not yet implemented")
                }

                override fun onPreloaded() {
                    //ODO("Not yet implemented")
                }

            }
        )
    }
}