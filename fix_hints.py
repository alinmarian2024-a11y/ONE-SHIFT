with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    text = f.read()

target = """                                    val text = if (currentLevel <= 5) {
                                        "INDICIU GRATUIT"
                                    } else if (hintState.totalHints > 0) {
                                        "INDICIU • ${hintState.totalHints}"
                                    } else {
                                        "VEZI VIDEO • +1 INDICIU"
                                    }"""

replacement = """                                    val text = if (currentLevel <= 5) {
                                        stringResource(R.string.free_hint).uppercase()
                                    } else if (hintState.totalHints > 0) {
                                        stringResource(R.string.hint_count, hintState.totalHints).uppercase()
                                    } else {
                                        stringResource(R.string.watch_video_hint).uppercase()
                                    }"""

text = text.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(text)
