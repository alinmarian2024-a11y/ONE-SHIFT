with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_code = """                onReplay = {
                    playerBoard = puzzleData.initialPlayerBoard
                    movesCount = 0
                    currentSolutionPath = puzzleData.solutionMoves
                    showHint = false
                    isSolved = false
                    levelFinished = false
                    inputLocked = false
                    replayCount++
                },
                onMenu = onBackToMenu,"""

new_code = """                onReplay = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = {
                            playerBoard = puzzleData.initialPlayerBoard
                            movesCount = 0
                            currentSolutionPath = puzzleData.solutionMoves
                            showHint = false
                            isSolved = false
                            levelFinished = false
                            inputLocked = false
                            replayCount++
                        }
                    )
                },
                onMenu = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = onBackToMenu
                    )
                },"""

content = content.replace(old_code, new_code)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
