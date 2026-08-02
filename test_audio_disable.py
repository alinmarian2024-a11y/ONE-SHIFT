import re
with open("app/src/main/java/com/example/GameAudioManager.kt", "r") as f:
    text = f.read()

text = text.replace("initSoundPool()", "// initSoundPool()")
text = text.replace("initMediaPlayer()", "// initMediaPlayer()")

with open("app/src/main/java/com/example/GameAudioManager.kt", "w") as f:
    f.write(text)
