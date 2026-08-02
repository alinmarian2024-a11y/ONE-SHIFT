import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Make sure GameScreen has adManager and rewardedHintAdProvider
if "adManager: AdManager" not in text:
    text = re.sub(r'(fun GameScreen\([\s\S]*?onRestorePurchases: \(\) -> Unit)(\))', r'\1,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider\2', text)

# Pass them in the usage
if "adManager = adManager" not in text:
    text = text.replace('onRestorePurchases = { billingRepository.restorePurchases() }\n                        )', 'onRestorePurchases = { billingRepository.restorePurchases() },\n                            adManager = adManager,\n                            rewardedHintAdProvider = rewardedHintAdProvider\n                        )')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
