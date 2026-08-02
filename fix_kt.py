import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Make sure GameScreen has adManager and rewardedHintAdProvider
if "adManager: AdManager" not in text:
    text = re.sub(r'(fun GameScreen\([\s\S]*?onRestorePurchases: \(\) -> Unit)(\))', r'\1,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider\2', text)
    
if "adManager: AdManager" not in text:
    text = text.replace("fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit\n)", 
                        "fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider\n)")
    
    text = text.replace("fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit) {", 
                        "fun GameScreen(\n    modifier: Modifier = Modifier,\n    initialLevel: Int,\n    prefs: android.content.SharedPreferences,\n    onBackToMenu: () -> Unit,\n    isAdFree: Boolean,\n    onRemoveAds: () -> Unit,\n    onRestorePurchases: () -> Unit,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider) {")


with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
