with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

lines = text.splitlines()
print(lines[1243:1248])
