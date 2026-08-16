import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Add verticalScroll import if missing
if "import androidx.compose.foundation.verticalScroll" not in content:
    content = content.replace("import androidx.compose.foundation.layout.fillMaxHeight", "import androidx.compose.foundation.layout.fillMaxHeight\nimport androidx.compose.foundation.verticalScroll\nimport androidx.compose.foundation.rememberScrollState")

# 2. Add verticalScroll to the main Column inside MainMenuScreen
column_search = """        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 100.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {"""

column_replace = """        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {"""

if column_search in content:
    content = content.replace(column_search, column_replace)
else:
    print("Could not find Column in MainMenuScreen")

# 3. Remove Spacer(modifier = Modifier.weight(1f))
spacer_search = "            Spacer(modifier = Modifier.weight(1f))\n"
if spacer_search in content:
    content = content.replace(spacer_search, "")
else:
    print("Could not find Spacer in MainMenuScreen")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
