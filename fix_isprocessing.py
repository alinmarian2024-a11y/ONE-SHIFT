import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Replace the inner definition
text = text.replace("var isProcessingExit by remember { mutableStateOf(false) }", "")

# Add it just before the dialog
dialog_start = r"        if \(showExitDialog\) \{"
new_dialog_start = """        var isProcessingExit by remember { mutableStateOf(false) }
        if (showExitDialog) {"""

text = re.sub(dialog_start, new_dialog_start, text)

# Also need to disable the dismiss button
text = text.replace("""                dismissButton = {
                    TextButton(onClick = {""", """                dismissButton = {
                    TextButton(
                        enabled = !isProcessingExit,
                        onClick = {""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
