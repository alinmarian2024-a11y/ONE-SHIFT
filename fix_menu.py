import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = re.sub(r"@Composable\s*@Composable\s*fun AnimatedMenuButton", "@Composable\nfun AnimatedMenuButton", content, flags=re.DOTALL)
content = re.sub(r"fun MainMenuScreen\s*\(", "@Composable\nfun MainMenuScreen(", content, flags=re.DOTALL)
# And just in case MainMenuScreen got multiple @Composable
content = re.sub(r"@Composable\s*@Composable\s*fun MainMenuScreen", "@Composable\nfun MainMenuScreen", content, flags=re.DOTALL)


with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

