package com.example

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface RewardedHintAdProvider {
    fun loadAndShow(activity: Activity, onReward: () -> Unit, onFailedOrClosed: () -> Unit)
}

class FakeRewardedHintAdProvider : RewardedHintAdProvider {
    override fun loadAndShow(activity: Activity, onReward: () -> Unit, onFailedOrClosed: () -> Unit) {
        if (com.example.BuildConfig.DEBUG) {
            // Show a mock toast/overlay
            android.widget.Toast.makeText(activity, "RECLAMĂ TEST (3s)", android.widget.Toast.LENGTH_SHORT).show()
            
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                onReward()
            }, 3000)
        } else {
            // In release, fail immediately if no real ad provider is integrated
            onFailedOrClosed()
        }
    }
}
