import re

with open("app/src/main/java/com/example/AdManager.kt", "r") as f:
    text = f.read()

fix_code = """
    private val attributedContext: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        context.createAttributionContext("admob")
    } else {
        context
    }
    
    private var consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(attributedContext)
"""

if "attributedContext" not in text:
    text = text.replace("    private var consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)", fix_code)
    text = text.replace("MobileAds.initialize(context)", "MobileAds.initialize(attributedContext)")
    text = text.replace("InterstitialAd.load(context", "InterstitialAd.load(attributedContext")

with open("app/src/main/java/com/example/AdManager.kt", "w") as f:
    f.write(text)
