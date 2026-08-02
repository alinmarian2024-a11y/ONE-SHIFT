import re

file_path = 'app/src/main/java/com/example/GameAudioManager.kt'
with open(file_path, 'r') as f:
    text = f.read()

text = re.sub(
    r'private val context = if \(android\.os\.Build\.VERSION\.SDK_INT >= android\.os\.Build\.VERSION_CODES\.S\) \{ baseContext\.createAttributionContext\("GameAudio"\) \} else \{ baseContext \}',
    r'private val context = baseContext',
    text
)

with open(file_path, 'w') as f:
    f.write(text)
