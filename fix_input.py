import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace("var inputLocked by remember { mutableStateOf(false) }", "var inputLocked by remember { mutableStateOf(false) }\n    val canAcceptGameInput = !inputLocked && !showExitDialog && !showSettings && !adManager.isAdShowing && !levelFinished")

text = text.replace("interactable = !isSolved && !inputLocked,", "interactable = !isSolved && canAcceptGameInput,")
text = text.replace("isInputLocked = inputLocked,", "isInputLocked = !canAcceptGameInput,")
text = text.replace("enabled = !inputLocked", "enabled = canAcceptGameInput")
text = text.replace("enabled = !inputLocked && !isAdLoading && !showHint && !isCalculatingHint,", "enabled = canAcceptGameInput && !isAdLoading && !showHint && !isCalculatingHint,")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
