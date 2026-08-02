with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Fix the buttons inside AlertDialogs
text = text.replace("TextButton(enabled = !showExitAppDialog && !showNewGameDialog, onClick = { \n                    showExitAppDialog = false", "TextButton(onClick = { \n                    showExitAppDialog = false")
text = text.replace("TextButton(enabled = !showExitAppDialog && !showNewGameDialog, onClick = { showExitAppDialog = false })", "TextButton(onClick = { showExitAppDialog = false })")
text = text.replace("TextButton(enabled = !showExitAppDialog && !showNewGameDialog, onClick = { \n                    showNewGameDialog = false", "TextButton(onClick = { \n                    showNewGameDialog = false")
text = text.replace("TextButton(enabled = !showExitAppDialog && !showNewGameDialog, onClick = { showNewGameDialog = false })", "TextButton(onClick = { showNewGameDialog = false })")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
