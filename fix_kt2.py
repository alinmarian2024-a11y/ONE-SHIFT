with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Replace explicitly
text = text.replace("    onRestorePurchases: () -> Unit\n) {", "    onRestorePurchases: () -> Unit,\n    adManager: AdManager,\n    rewardedHintAdProvider: RewardedHintAdProvider\n) {")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
