import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

text = text.replace("var showExitDialog by remember { mutableStateOf(false) }", "var showExitDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }")

dialog_code = """        if (showExitDialog) {
            AlertDialog(
                properties = androidx.compose.ui.window.DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                ),
                onDismissRequest = { 
                    // Do nothing here because we want explicit button press
                },
                title = { Text("Ești sigur?", color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text("Vei pierde mutările făcute în această încercare. Nivelurile terminate, stelele și recordurile rămân salvate.", color = Color.Gray) },
                confirmButton = {
                    var isProcessingExit by remember { mutableStateOf(false) }
                    TextButton(
                        enabled = !isProcessingExit,
                        onClick = { 
                            if (!isProcessingExit) {
                                isProcessingExit = true
                                adManager.showPendingInterstitialIfAny(
                                    activity = context as android.app.Activity,
                                    isAdFree = isAdFree,
                                    onFinished = {
                                        showExitDialog = false
                                        inputLocked = false
                                        onBackToMenu()
                                    }
                                )
                            }
                        }
                    ) { Text("DA, IEȘI", color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        showExitDialog = false
                        inputLocked = false
                    }) { Text("REVENIRE ÎN JOC", color = Color.Gray) }
                },
                containerColor = Color(0xFF2B2930)
            )
        }"""

# Need to replace the old dialog
old_dialog_regex = r"        if \(showExitDialog\) \{\s*AlertDialog\(\s*onDismissRequest = \{\s*showExitDialog = false\s*inputLocked = false\s*\},.*?containerColor = Color\(0xFF2B2930\)\s*\)\s*\}"

text = re.sub(old_dialog_regex, dialog_code, text, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
