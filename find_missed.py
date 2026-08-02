import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

strings1 = re.findall(r'Text\(text = "([^"]+)"', text)
strings2 = re.findall(r'Text\("([^"]+)"', text)

for s in set(strings1 + strings2):
    print(s)
