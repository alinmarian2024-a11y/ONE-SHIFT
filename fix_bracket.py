import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

pattern = r'(Text\(stringResource\(R\.string\.exit_game\).*?\n\s*\})\n\s*\}\n\s*Column'
replacement = r'\1\n                }\n            }\n            Column'
new_text = re.sub(pattern, replacement, text)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(new_text)

