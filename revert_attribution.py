import re
import os

def revert_in_file(filepath, revert_func):
    if not os.path.exists(filepath):
        return
    with open(filepath, "r") as f:
        text = f.read()
    new_text = revert_func(text)
    if text != new_text:
        with open(filepath, "w") as f:
            f.write(new_text)

def revert_game_audio_manager(text):
    text = re.sub(r'var attributedContext = context\s+if \(android\.os\.Build\.VERSION\.SDK_INT >= android\.os\.Build\.VERSION_CODES\.S\) \{\s+attributedContext = context\.createAttributionContext\("game_audio"\)\s+\}', '', text)
    text = text.replace('initSoundPool(attributedContext)', 'initSoundPool(context)')
    text = text.replace('initMediaPlayer(attributedContext)', 'initMediaPlayer(context)')
    return text

revert_in_file("app/src/main/java/com/example/GameAudioManager.kt", revert_game_audio_manager)

def revert_ad_manager(text):
    text = re.sub(r'private val attributedContext: Context = if \(android\.os\.Build\.VERSION\.SDK_INT >= android\.os\.Build\.VERSION_CODES\.S\) \{\s+context\.createAttributionContext\("admob"\)\s+\} else \{\s+context\s+\}', '', text)
    text = text.replace('UserMessagingPlatform.getConsentInformation(attributedContext)', 'UserMessagingPlatform.getConsentInformation(context)')
    text = text.replace('MobileAds.initialize(attributedContext)', 'MobileAds.initialize(context)')
    text = text.replace('InterstitialAd.load(attributedContext', 'InterstitialAd.load(context')
    return text

revert_in_file("app/src/main/java/com/example/AdManager.kt", revert_ad_manager)

def revert_admob_reward(text):
    text = re.sub(r'private val attributedContext: Context = if \(android\.os\.Build\.VERSION\.SDK_INT >= android\.os\.Build\.VERSION_CODES\.S\) \{\s+context\.createAttributionContext\("admob"\)\s+\} else \{\s+context\s+\}', '', text)
    text = text.replace('RewardedAd.load(attributedContext', 'RewardedAd.load(context')
    return text

revert_in_file("app/src/main/java/com/example/AdMobRewardedHintAdProvider.kt", revert_admob_reward)

def revert_billing(text):
    text = re.sub(r'val attributedContext = if \(android\.os\.Build\.VERSION\.SDK_INT >= android\.os\.Build\.VERSION_CODES\.S\) \{\s+context\.createAttributionContext\("billing"\)\s+\} else \{\s+context\s+\}', '', text)
    text = text.replace('billingClient = BillingClient.newBuilder(attributedContext)', 'billingClient = BillingClient.newBuilder(context)')
    return text

revert_in_file("app/src/main/java/com/example/BillingRepository.kt", revert_billing)

def revert_main_activity(text):
    text = re.sub(r'override fun getAttributionTag\(\): String\? \{\s+return "main"\s+\}', '', text)
    return text

revert_in_file("app/src/main/java/com/example/MainActivity.kt", revert_main_activity)

def revert_manifest(text):
    text = re.sub(r'<attribution[^>]+>\s*', '', text)
    return text

revert_in_file("app/src/main/AndroidManifest.xml", revert_manifest)

