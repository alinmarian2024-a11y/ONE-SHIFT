import re

with open("app/src/main/java/com/example/GameAudioManager.kt", "r") as f:
    text = f.read()

# We need to uncomment the audio initialization and fix the AppOps attributionTag error.
# The AppOps error usually occurs on Android 12+ if we use a context for audio that is not correctly attributed.
# The context used is `context.applicationContext`.
# To fix it, we should ensure the context has a proper attribution tag or we create one.
# Given it's a game, we can just use the activity context, or create an attribution context if the API level allows.
# For simplicity, we can create an attribution context.

fix_code = """
        var attributedContext = context
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            attributedContext = context.createAttributionContext("game_audio")
        }
"""

if "attributedContext" not in text:
    text = text.replace("init {", "init {\n" + fix_code)

# Then replace context with attributedContext in initSoundPool
text = text.replace("var builder = AudioAttributes.Builder()", "val builder = AudioAttributes.Builder()")
# And also we need to uncomment initSoundPool and initMediaPlayer
text = text.replace("// initSoundPool(context)", "initSoundPool(attributedContext)")
text = text.replace("// initMediaPlayer(context)", "initMediaPlayer(attributedContext)")

# Also uncomment the actual playback lines
text = text.replace("// soundPool?.play", "soundPool?.play")
text = text.replace("// mediaPlayer?.start()", "mediaPlayer?.start()")
text = text.replace("// mediaPlayer?.pause()", "mediaPlayer?.pause()")

with open("app/src/main/java/com/example/GameAudioManager.kt", "w") as f:
    f.write(text)
