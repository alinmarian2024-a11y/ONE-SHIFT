with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace("var showExitAppDialog by remember { mutableStateOf(false) }", "var showExitAppDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
