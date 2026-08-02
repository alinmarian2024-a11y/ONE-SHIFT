import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Add adManager and rewardedHintAdProvider to GameScreen parameters
if "adManager: AdManager" not in text:
    text = re.sub(r'(fun GameScreen\(\s*modifier: Modifier = Modifier,\s*initialLevel: Int,\s*prefs: android\.content\.SharedPreferences,\s*onBackToMenu: \(\) -> Unit,\s*isAdFree: Boolean,\s*onRemoveAds: \(\) -> Unit,\s*onRestorePurchases: \(\) -> Unit)', 
                  r'\1,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider', text)

# Find where GameScreen is called and pass them
text = text.replace('onRestorePurchases = { billingRepository.restorePurchases() }\n                        )', 
                    'onRestorePurchases = { billingRepository.restorePurchases() },\n                            adManager = adManager,\n                            rewardedHintAdProvider = rewardedHintAdProvider\n                        )')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
