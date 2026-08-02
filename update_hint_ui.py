import re

file_path = 'app/src/main/java/com/example/MainActivity.kt'
with open(file_path, 'r') as f:
    text = f.read()

# Replace actualHintMove declaration
text = re.sub(
    r'val actualHintMove = currentSolutionPath\.firstOrNull\(\)',
    r'var actualHintMove by remember { mutableStateOf<Move?>(null) }\n    var isCalculatingHint by remember { mutableStateOf(false) }',
    text
)

# Add LaunchedEffect for actualHintMove calculation
effect_code = """
    LaunchedEffect(playerBoard, currentLevel, replayCount) {
        if (!isSolved) {
            isCalculatingHint = true
            actualHintMove = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                Solver.getHintMove(playerBoard, puzzleData.targetBoard, currentSolutionPath)
            }
            isCalculatingHint = false
        }
    }
"""

text = re.sub(
    r'val hintMove = if \(isTutorial \|\| showHint\) actualHintMove else null',
    r'val hintMove = if (isTutorial || showHint) actualHintMove else null' + effect_code,
    text
)

# Modify Hint Button
old_hint_button = """
                                if (currentLevel <= 5) {
                                    showHint = true
                                } else {
                                    if (hintState.totalHints > 0) {
                                        if (!showHint) {
                                            if (actualHintMove != null) {
                                                hintRepository.consumeHint()
                                                showHint = true
                                            } else {
                                                HintEventBus.emitEvent("Nu există o mutare garantată. Folosește Reset!")
                                            }
                                        }
                                    } else {
                                        val activity = context as? android.app.Activity
                                        if (activity != null && !isAdLoading) {
                                            isAdLoading = true
                                            FakeRewardedHintAdProvider().loadAndShow(
                                                activity,
                                                onReward = { 
                                                    hintRepository.addRewardedAdHint()
                                                    isAdLoading = false 
                                                },
                                                onFailedOrClosed = { isAdLoading = false }
                                            )
                                        }
                                    }
                                }
"""

new_hint_button = """
                                if (currentLevel <= 5) {
                                    if (actualHintMove != null) {
                                        showHint = true
                                    } else {
                                        HintEventBus.emitEvent("Nu există o mutare garantată. Folosește Reset!")
                                    }
                                } else {
                                    if (hintState.totalHints > 0) {
                                        if (!showHint) {
                                            if (actualHintMove != null) {
                                                hintRepository.consumeHint()
                                                showHint = true
                                            } else {
                                                HintEventBus.emitEvent("Nu există o mutare garantată. Folosește Reset!")
                                            }
                                        }
                                    } else {
                                        val activity = context as? android.app.Activity
                                        if (activity != null && !isAdLoading) {
                                            isAdLoading = true
                                            FakeRewardedHintAdProvider().loadAndShow(
                                                activity,
                                                onReward = { 
                                                    hintRepository.addRewardedAdHint()
                                                    isAdLoading = false 
                                                },
                                                onFailedOrClosed = { isAdLoading = false }
                                            )
                                        }
                                    }
                                }
"""

text = text.replace(old_hint_button.strip(), new_hint_button.strip())

# Fix enabled state of Hint Button
text = re.sub(
    r'enabled = !inputLocked && !isAdLoading && !showHint,',
    r'enabled = !inputLocked && !isAdLoading && !showHint && !isCalculatingHint,',
    text
)

# Hint text inside button (SE ÎNCARCĂ)
text = re.sub(
    r'\} else if \(isAdLoading\) \{',
    r'} else if (isAdLoading || isCalculatingHint) {',
    text
)

with open(file_path, 'w') as f:
    f.write(text)
