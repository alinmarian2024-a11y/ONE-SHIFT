import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace('                onRemoveAds = onRemoveAds,\n                onRestorePurchases = onRestorePurchases\n            )', '                onRemoveAds = onRemoveAds,\n                onRestorePurchases = onRestorePurchases,\n                adManager = adManager,\n                rewardedHintAdProvider = rewardedHintAdProvider\n            )')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
