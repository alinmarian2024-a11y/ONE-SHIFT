import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

old_dialog_regex = r"    if \(showExitAppDialog\) \{\s*AlertDialog\(\s*onDismissRequest = \{ showExitAppDialog = false \},\s*title = \{ Text\(\"Ieși din joc\?\", color = Color.White, fontWeight = FontWeight.Bold\) \},\s*text = \{ Text\(\"Progresul și setările tale sunt salvate automat.\", color = Color.Gray\) \},\s*confirmButton = \{\s*TextButton\(onClick = \{\s*showExitAppDialog = false\s*onExitApp\(\)\s*\}\) \{ Text\(\"DA, ÎNCHIDE\", color = Color\(0xFFD0BCFF\), fontWeight = FontWeight.Bold\) \}\s*\},\s*dismissButton = \{\s*TextButton\(onClick = \{ showExitAppDialog = false \}\) \{ Text\(\"RĂMÂN ÎN JOC\", color = Color.Gray\) \}\s*\},\s*containerColor = Color\(0xFF2B2930\)\s*\)\s*\}"

new_dialog = """    if (showExitAppDialog) {
        AlertDialog(
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            onDismissRequest = { 
                // Do nothing
            },
            title = { Text("Ieși din joc?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Progresul și setările tale sunt salvate automat.", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = { 
                    showExitAppDialog = false
                    onExitApp()
                }) { Text("DA, IEȘI", color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showExitAppDialog = false }) { Text("REVINO LA MENIU", color = Color.Gray) }
            },
            containerColor = Color(0xFF2B2930)
        )
    }"""

text = re.sub(old_dialog_regex, new_dialog, text, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
