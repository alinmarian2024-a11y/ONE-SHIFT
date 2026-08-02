import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

strings = re.findall(r'Text\("([^"]+)"', text)
for s in sorted(set(strings)):
    print(s)
