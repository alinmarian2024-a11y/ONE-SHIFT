package com.example

import android.content.Context
import com.example.R
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class HintState(
    val dailyHintsRemaining: Int = 0,
    val bonusHintsBalance: Int = 0,
    val initialBonusGranted: Boolean = false,
    val lastDailyHintEpochDay: Long = 0L,
    val rewardedThresholds: Set<String> = emptySet()
) {
    val totalHints get() = dailyHintsRemaining + bonusHintsBalance
}

class HintRepository private constructor(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)
    private val _hintState = MutableStateFlow(loadState())
    val hintState: StateFlow<HintState> = _hintState.asStateFlow()

    init {
        checkInitialBonus()
        checkDailyReset()
    }

    private fun loadState(): HintState {
        return HintState(
            dailyHintsRemaining = prefs.getInt("daily_hints", 0),
            bonusHintsBalance = prefs.getInt("bonus_hints", 0),
            initialBonusGranted = prefs.getBoolean("initial_hint_bonus_granted", false),
            lastDailyHintEpochDay = prefs.getLong("last_daily_hint_epoch_day", 0L),
            rewardedThresholds = prefs.getStringSet("rewarded_thresholds", emptySet())?.toSet() ?: emptySet()
        )
    }

    private fun saveState(state: HintState) {
        prefs.edit()
            .putInt("daily_hints", state.dailyHintsRemaining)
            .putInt("bonus_hints", state.bonusHintsBalance)
            .putBoolean("initial_hint_bonus_granted", state.initialBonusGranted)
            .putLong("last_daily_hint_epoch_day", state.lastDailyHintEpochDay)
            .putStringSet("rewarded_thresholds", state.rewardedThresholds)
            .apply()
    }

    private fun checkInitialBonus() {
        _hintState.update { current ->
            if (!current.initialBonusGranted) {
                val newState = current.copy(
                    dailyHintsRemaining = 2,
                    bonusHintsBalance = 3,
                    initialBonusGranted = true,
                    lastDailyHintEpochDay = LocalDate.now().toEpochDay()
                )
                saveState(newState)
                newState
            } else {
                current
            }
        }
    }

    // Returns true if daily hint balance increased
    fun checkDailyReset(): Boolean {
        var increased = false
        _hintState.update { current ->
            val today = LocalDate.now().toEpochDay()
            if (today > current.lastDailyHintEpochDay) {
                val newDaily = 2
                if (newDaily > current.dailyHintsRemaining) {
                    increased = true
                }
                val newState = current.copy(
                    dailyHintsRemaining = newDaily,
                    lastDailyHintEpochDay = today
                )
                saveState(newState)
                if (increased) {
                    HintEventBus.emitEvent(context.getString(R.string.daily_bonus, newDaily))
                }
                newState
            } else {
                current
            }
        }
        return increased
    }

    fun consumeHint(): Boolean {
        var consumed = false
        _hintState.update { current ->
            if (current.dailyHintsRemaining > 0) {
                consumed = true
                val newState = current.copy(dailyHintsRemaining = current.dailyHintsRemaining - 1)
                saveState(newState)
                newState
            } else if (current.bonusHintsBalance > 0) {
                consumed = true
                val newState = current.copy(bonusHintsBalance = current.bonusHintsBalance - 1)
                saveState(newState)
                newState
            } else {
                current
            }
        }
        return consumed
    }

    fun addDailyShiftHint() {
        _hintState.update { current ->
            val newState = current.copy(bonusHintsBalance = current.bonusHintsBalance + 1)
            saveState(newState)
            newState
        }
    }

    fun addRewardedAdHint() {
        _hintState.update { current ->
            val newState = current.copy(bonusHintsBalance = current.bonusHintsBalance + 1)
            saveState(newState)
            HintEventBus.emitEvent(context.getString(R.string.hint_received))
            newState
        }
    }

    fun resetForNewCampaign() {
        _hintState.update {
            val newState = HintState(
                dailyHintsRemaining = 2,
                bonusHintsBalance = 3,
                initialBonusGranted = true,
                lastDailyHintEpochDay = LocalDate.now().toEpochDay(),
                rewardedThresholds = emptySet()
            )
            saveState(newState)
            newState
        }
    }

    // Returns true if bonus was granted
    fun checkAndGrantThresholdBonus(level: Int, localizedContext: Context? = null): Boolean {
        if (level % 10 != 0 || level == 0) return false
        
        var granted = false
        _hintState.update { current ->
            val levelKey = level.toString()
            if (!current.rewardedThresholds.contains(levelKey)) {
                granted = true
                val newSet = current.rewardedThresholds + levelKey
                val newState = current.copy(
                    bonusHintsBalance = current.bonusHintsBalance + 1,
                    rewardedThresholds = newSet
                )
                saveState(newState)
                val ctx = localizedContext ?: context
                HintEventBus.emitEvent(ctx.getString(R.string.progress_bonus))
                newState
            } else {
                current
            }
        }
        return granted
    }

    companion object {
        @Volatile private var instance: HintRepository? = null
        fun getInstance(context: Context): HintRepository =
            instance ?: synchronized(this) {
                instance ?: HintRepository(context.applicationContext).also { instance = it }
            }
    }
}
