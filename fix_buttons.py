with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# We need to only replace this inside MainMenuScreen
start_idx = text.find("fun MainMenuScreen(")
end_idx = text.find("fun LevelSelectScreen(")

main_menu_text = text[start_idx:end_idx]

main_menu_text = main_menu_text.replace("Button(onClick =", "Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick =")
main_menu_text = main_menu_text.replace("Button(\n            onClick = { showExitAppDialog = true },", "Button(\n            enabled = !showExitAppDialog && !showNewGameDialog,\n            onClick = { showExitAppDialog = true },")

text = text[:start_idx] + main_menu_text + text[end_idx:]

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
