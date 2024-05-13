package com.aman.adscontroller.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aman.ads_manager.callbacks.OnShowAdCompleteListener
import com.aman.ads_manager.concent.GoogleMobileAdsConsentManager
import com.aman.adscontroller.AppClass
import com.aman.adscontroller.MainActivity
import com.aman.adscontroller.R
import com.aman.adscontroller.databinding.ActivitySplashBinding
import com.google.android.gms.ads.MobileAds
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private const val COUNTER_TIME_MILLISECONDS = 5000L

class SplashActivity : AppCompatActivity() {
    private val TAG = SplashActivity::class.java.simpleName

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private var secondsRemaining: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
            Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())

            // Create a timer so the SplashActivity will be displayed for a fixed amount of time.
            createTimer(COUNTER_TIME_MILLISECONDS)

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

            // This sample attempts to load ads using consent obtained in the previous session.
            if (googleMobileAdsConsentManager.canRequestAds) {
                initializeMobileAdsSdk()
            }
        }

        /**
         * Create the countdown timer, which counts down to zero and show the app open ad.
         *
         * @param time the number of milliseconds that the timer counts down from
         */
        private fun createTimer(time: Long) {
            val counterTextView: TextView = findViewById(R.id.txtSplashLoading)
            val countDownTimer: CountDownTimer =
                object : CountDownTimer(time, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                        counterTextView.text = "App is done loading in: $secondsRemaining"
                    }

                    override fun onFinish() {
                        secondsRemaining = 0
                        counterTextView.text = "Done."

                        (application as AppClass).showAdIfAvailable(
                            this@SplashActivity,
                            object : OnShowAdCompleteListener {
                                override fun onShowAdComplete() {
                                    // Check if the consent form is currently on screen before moving to the main
                                    // activity.
                                    if (googleMobileAdsConsentManager.canRequestAds) {
                                        startMainActivity()
                                    }
                                }
                            }
                        )
                    }
                }
            countDownTimer.start()
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

        /** Start the MainActivity. */
        fun startMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }