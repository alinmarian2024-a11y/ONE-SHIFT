import re

with open("app/src/main/java/com/example/AdMobRewardedHintAdProvider.kt", "r") as f:
    text = f.read()

fix_code = """
    private val attributedContext: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        context.createAttributionContext("admob")
    } else {
        context
    }
"""

if "attributedContext" not in text:
    text = text.replace("    private var rewardedAd: RewardedAd? = null", fix_code + "    private var rewardedAd: RewardedAd? = null")
    text = text.replace("RewardedAd.load(context", "RewardedAd.load(attributedContext")

with open("app/src/main/java/com/example/AdMobRewardedHintAdProvider.kt", "w") as f:
    f.write(text)
