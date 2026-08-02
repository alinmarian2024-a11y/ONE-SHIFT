import re

with open("app/src/main/java/com/example/GameAudioManager.kt", "r") as f:
    text = f.read()

text = text.replace("mediaPlayer?.start()", "// mediaPlayer?.start()")
text = text.replace("mediaPlayer?.pause()", "// mediaPlayer?.pause()")
text = text.replace("soundPool?.play", "// soundPool?.play")

with open("app/src/main/java/com/example/GameAudioManager.kt", "w") as f:
    f.write(text)
