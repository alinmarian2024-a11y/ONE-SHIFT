import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Add DisposableEffect
effect_code = """
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val isAppInForeground = lifecycleState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)
    
    val isActive = !levelFinished && !showExitDialog && !showSettings && isAppInForeground && !adManager.isAdShowing

    DisposableEffect(isActive) {
        if (isActive) {
            adManager.startTracking()
        } else {
            adManager.stopTracking()
        }
        onDispose {
            adManager.stopTracking()
        }
    }
"""

if "val isActive = !levelFinished" not in text:
    # Insert after `var showSettings by remember { mutableStateOf(false) }` (which is at the top of GameScreen)
    text = re.sub(r'(var showSettings by remember \{ mutableStateOf\(false\) \})', r'\1\n' + effect_code, text)

# Now, intercept `onNextLevel` and `onBackToMenu` to show the interstitial ad if needed.
# Let's find onNextLevel inside CompletionDialog
text = text.replace('onNextLevel = { currentLevel++ },', 
"""onNextLevel = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = { currentLevel++ }
                    )
                },""")

# Replace onBackToMenu inside GameScreen (exit dialog)
text = text.replace('onClick = { onBackToMenu() }',
"""onClick = { 
                                adManager.showPendingInterstitialIfAny(
                                    activity = context as android.app.Activity,
                                    isAdFree = isAdFree,
                                    onFinished = onBackToMenu
                                )
                            }""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
