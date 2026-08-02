package com.example

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobRewardedHintAdProvider(
    private val context: Context,
    private val adManager: AdManager
) : RewardedHintAdProvider {

    
    private var rewardedAd: RewardedAd? = null
    private var isAdLoading = false

    init {
        loadAd()
    }

    private fun loadAd() {
        if (rewardedAd != null || isAdLoading) return
        isAdLoading = true

        val adRequest = AdRequest.Builder().build()
        // Test ID
        val adUnitId = "ca-app-pub-3940256099942544/5224354917"

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                isAdLoading = false
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                isAdLoading = false
            }
        })
    }

    override fun loadAndShow(activity: Activity, onReward: () -> Unit, onFailedOrClosed: () -> Unit) {
        if (rewardedAd == null) {
            // Attempt to load and just fail for now so user isn't blocked
            loadAd()
            onFailedOrClosed()
            return
        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                loadAd()
                onFailedOrClosed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                loadAd()
                onFailedOrClosed()
            }
        }

        rewardedAd?.show(activity) {
            // On user earned reward
            adManager.onRewardedAdShown()
            onReward()
        }
    }
}
