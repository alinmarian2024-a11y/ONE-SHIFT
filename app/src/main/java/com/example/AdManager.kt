package com.example

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean

class AdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)

    
    
    private var consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)


    var interstitialPending = false
    var isAdShowing = false
    
    // Timer tracking
    private var accumulatedActiveTimeMs: Long = 0
    private var sessionStartTimeMs: Long = 0
    private var isTracking = false

    private val THRESHOLD_MS = 13 * 60 * 1000L // 13 minutes

    fun initialize(activity: Activity) {
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (consentInformation.canRequestAds()) {
                        initializeMobileAdsSdk()
                    }
                }
            },
            { requestConsentError ->
                // Consent gathering failed, but we can still initialize if we have consent
                if (consentInformation.canRequestAds()) {
                    initializeMobileAdsSdk()
                }
            }
        )
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) return
        MobileAds.initialize(context)
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        if (interstitialAd != null || isAdLoading || !consentInformation.canRequestAds()) return
        isAdLoading = true

        val adRequest = AdRequest.Builder().build()
        // Use test ID
        val adUnitId = "ca-app-pub-3940256099942544/1033173712"
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                isAdLoading = false
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                isAdLoading = false
            }
        })
    }

    fun startTracking() {
        if (!isTracking) {
            isTracking = true
            sessionStartTimeMs = SystemClock.elapsedRealtime()
        }
    }

    fun stopTracking() {
        if (isTracking) {
            isTracking = false
            val elapsed = SystemClock.elapsedRealtime() - sessionStartTimeMs
            accumulatedActiveTimeMs += elapsed
            checkThreshold()
        }
    }

    private fun checkThreshold() {
        if (accumulatedActiveTimeMs >= THRESHOLD_MS) {
            interstitialPending = true
        }
    }

    fun showPendingInterstitialIfAny(activity: Activity, onFinished: () -> Unit, isAdFree: Boolean) {
        if (isTracking) {
            stopTracking()
            checkThreshold()
            startTracking()
        } else {
            checkThreshold()
        }

        if (!interstitialPending || isAdFree) {
            onFinished()
            return
        }

        if (interstitialAd == null) {
            // Failed to load or not loaded yet.
            // DO NOT block the player. Keep it pending.
            // Attempt to load for next time.
            loadInterstitialAd()
            onFinished()
            return
        }

        isAdShowing = true
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                isAdShowing = false
                interstitialPending = false
                accumulatedActiveTimeMs = 0
                loadInterstitialAd()
                onFinished()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                isAdShowing = false
                // Keep pending if it failed to show? Instructions say:
                // "If the ad was unavailable, keep the advertisement pending for a future natural transition, but use a safe retry delay and never interrupt gameplay."
                loadInterstitialAd()
                onFinished()
            }
        }
        interstitialAd?.show(activity)
    }

    fun onRewardedAdShown() {
        // "After any rewarded video is shown, restart the 13-minute interstitial cooldown to prevent back-to-back full-screen ads."
        accumulatedActiveTimeMs = 0
        interstitialPending = false
        // Reload interstitial if we consumed it or if we just want to be sure it's ready
        if (interstitialAd == null) {
            loadInterstitialAd()
        }
    }
}
