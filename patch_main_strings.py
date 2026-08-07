import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace('text = "Nivelul $currentLevel"', 'text = stringResource(R.string.level_title, currentLevel)')
content = content.replace('text = "Mutări: $movesCount / ${puzzleData.config.moves}"', 'text = stringResource(R.string.moves_count, movesCount, puzzleData.config.moves)')
content = content.replace('text = "MODEL"', 'text = stringResource(R.string.target_pattern)')
content = content.replace('text = "Glisează rândul indicat pentru a reface modelul"', 'text = stringResource(R.string.tutorial_swipe)')
content = content.replace('text = "GLISEAZĂ PENTRU A REFACE MODELUL"', 'text = stringResource(R.string.swipe_to_rebuild)')
content = content.replace('HintEventBus.emitEvent("Nu există o mutare garantată. Folosește Reset!")', 'HintEventBus.emitEvent(context.getString(R.string.no_guaranteed_move))')
content = content.replace('"INDICIU GRATUIT"', 'stringResource(R.string.free_hint)')
content = content.replace('"INDICIU • ${hintState.totalHints}"', 'stringResource(R.string.hint_count, hintState.totalHints)')
content = content.replace('"VEZI VIDEO • +1 INDICIU"', 'stringResource(R.string.watch_video_hint)')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
