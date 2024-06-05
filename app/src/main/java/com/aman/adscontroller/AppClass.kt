package com.aman.adscontroller

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.aman.ads_manager.admob.appopen.OpenAdManager
import com.aman.ads_manager.callbacks.OnShowAdCompleteListener
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

private const val AD_UNIT_ID = "/6499/example/app-open"

class AppClass: Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private val TAG = AppClass::class.java.simpleName

    private lateinit var context: Context

    private lateinit var appOpenAdManager: OpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        appInstance = this

        /*********Facebook ads***********/
        AudienceNetworkAds.initialize(this)
        // if app release change to false
        AdSettings.setTestMode(true)
        // Example for setting the SDK to crash when in debug mode
        AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE)
        /************<end>*****************/

        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = OpenAdManager(this, AD_UNIT_ID)


        MobileAds.initialize(this)

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("566E9CF2E5BF0F3F112621BA0CDD12B1", "25E8003CF5DD39E8AF2DE715AC192AF4")).build()
        )
    }

    companion object {
        private var appInstance: AppClass? = null
        fun getInstance(): AppClass {
            if (appInstance == null) {
                appInstance = AppClass()
            }
            return appInstance!!
        }

    }

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

    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        if (!MainActivity.isAdShowing)
            appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    fun loadAd(activity: Activity) {
        appOpenAdManager.loadAd(activity)
    }

}