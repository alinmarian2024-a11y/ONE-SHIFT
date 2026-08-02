import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# We need to add adManager and rewardedHintAdProvider to GameScreen properly!
if "fun GameScreen(" in text:
    # First, let's remove the broken DisposableEffect at the top of MainActivity onCreate if it's there.
    text = re.sub(r'val lifecycleOwner = androidx\.lifecycle\.compose\.LocalLifecycleOwner\.current[\s\S]*?adManager\.stopTracking\(\)\n\s*\}\n\s*\}', '', text)

    # Now add parameters to GameScreen if missing
    if "adManager: AdManager" not in text:
        text = text.replace("fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit\n)", 
                            "fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider\n)")
        
        text = text.replace("fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit) {", 
                            "fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider) {")

    # Now add the DisposableEffect inside GameScreen after `showExitDialog`
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
        text = text.replace("var showExitDialog by remember { mutableStateOf(false) }", "var showExitDialog by remember { mutableStateOf(false) }\n" + effect_code)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
