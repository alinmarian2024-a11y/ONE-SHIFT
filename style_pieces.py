with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace('CYAN(Color(0xFF00BCD4), "●")', 'CYAN(Color(0xFF00E5FF), "●")')
content = content.replace('VIOLET(Color(0xFF9C27B0), "◆")', 'VIOLET(Color(0xFFD500F9), "◆")')
content = content.replace('CORAL(Color(0xFFFF7F50), "■")', 'CORAL(Color(0xFFFF3D00), "■")')
content = content.replace('LIME(Color(0xFFCDDC39), "▲")', 'LIME(Color(0xFFAEEA00), "▲")')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
