with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_code = """                LaunchedEffect(Unit) {
                    HintEventBus.events.collect { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }"""

new_code = """                LaunchedEffect(Unit) {
                    kotlinx.coroutines.launch {
                        HintEventBus.events.collect { message ->
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                LaunchedEffect(currentScreen) {
                    val audioManager = GameAudioManager.getInstance(this@MainActivity)
                    if (currentScreen == ScreenState.GAME) {
                        audioManager.playGameplayMusic()
                    } else {
                        audioManager.playMenuTheme()
                    }
                }"""

content = content.replace(old_code, new_code)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
