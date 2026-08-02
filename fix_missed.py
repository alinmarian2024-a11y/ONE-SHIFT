import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace('text = "Nivelul $level",', 'text = stringResource(R.string.level_text, level),')
text = text.replace('text = "Nivelul $currentLevel",', 'text = stringResource(R.string.level_text, currentLevel),')
text = text.replace('text = "Mutări: $movesCount / ${puzzleData.config.moves}",', 'text = stringResource(R.string.moves_text, "$movesCount / ${puzzleData.config.moves}"),')
text = text.replace('text = "MODEL",', 'text = stringResource(R.string.model),')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
