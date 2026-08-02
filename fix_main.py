import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

if "override fun getAttributionTag()" not in text:
    text = text.replace("class MainActivity : ComponentActivity() {", "class MainActivity : ComponentActivity() {\n\n    override fun getAttributionTag(): String? {\n        return \"main\"\n    }\n")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
