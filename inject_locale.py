import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# 1. Add LanguageSelector composable at the end of the file
language_selector = """
@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLanguageChangeStarted: () -> Unit,
    onLanguageChangeEnded: () -> Unit
) {
    val currentConfig = androidx.compose.ui.platform.LocalConfiguration.current
    val currentLang = currentConfig.locales.get(0)?.language ?: "en"
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { 
                showDialog = true 
                onLanguageChangeStarted()
            },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
        ) {
            Text("🌐 ${currentLang.uppercase()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                // Do nothing
            },
            title = null,
            text = {
                Column {
                    val languages = listOf("en" to "English", "ro" to "Română", "es" to "Español", "it" to "Italiano")
                    languages.forEach { (code, name) ->
                        TextButton(
                            onClick = {
                                androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(androidx.core.os.LocaleListCompat.forLanguageTags(code))
                                showDialog = false
                                onLanguageChangeEnded()
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(name, color = Color.White, fontSize = 18.sp, fontWeight = if (code == currentLang) FontWeight.Bold else FontWeight.Normal)
                                if (code == currentLang) Text("✓", color = Color(0xFF38E887), fontSize = 18.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showDialog = false 
                    onLanguageChangeEnded()
                }) {
                    Text(stringResource(R.string.cancel), color = Color.Gray)
                }
            },
            containerColor = Color(0xFF2B2930),
            properties = androidx.compose.ui.window.DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
        )
    }
}
"""
text = text + language_selector

# 2. Inject initial locale check in onCreate
initial_locale_code = """
        val prefs = getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)
        val languageSet = prefs.getBoolean("language_set", false)
        if (!languageSet) {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(androidx.core.os.LocaleListCompat.forLanguageTags("en"))
            prefs.edit().putBoolean("language_set", true).apply()
        }
"""
text = text.replace('val prefs = getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)', initial_locale_code)

# 3. Inject into MainMenuScreen
main_menu_injection = """
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LanguageSelector(
                enabled = !showExitAppDialog && !showNewGameDialog,
                onLanguageChangeStarted = {},
                onLanguageChangeEnded = {}
            )
        }
    }
}
"""
# Replace the end of MainMenuScreen Box
text = text.replace('        }\n    }\n}\n\n@Composable\nfun LevelSelectScreen(', main_menu_injection + '\n\n@Composable\nfun LevelSelectScreen(')

# 4. Inject into GameScreen
game_screen_old = """
                TextButton(
                    onClick = {
                        showExitDialog = true
                        inputLocked = true
                    },
"""
game_screen_new = """
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LanguageSelector(
                        enabled = !showExitDialog && !levelFinished,
                        onLanguageChangeStarted = { inputLocked = true },
                        onLanguageChangeEnded = { inputLocked = false }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            showExitDialog = true
                            inputLocked = true
                        },
"""
text = text.replace(game_screen_old, game_screen_new)

# Add closing bracket for the new Row in GameScreen
game_screen_old_close = """
                    Text(stringResource(R.string.exit_game), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(
"""
game_screen_new_close = """
                    Text(stringResource(R.string.exit_game), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                }
            }
            Column(
"""
text = text.replace(game_screen_old_close, game_screen_new_close)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
