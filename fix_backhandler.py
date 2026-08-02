import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

back_handler = """    BackHandler(enabled = !levelFinished && !showExitDialog) {
        showExitDialog = true
        inputLocked = true
    }"""

old_back_handler_regex = r"    BackHandler\(enabled = !levelFinished\) \{\s*if \(showExitDialog\) \{\s*showExitDialog = false\s*inputLocked = false\s*\} else \{\s*showExitDialog = true\s*inputLocked = true\s*\}\s*\}"

text = re.sub(old_back_handler_regex, back_handler, text)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
