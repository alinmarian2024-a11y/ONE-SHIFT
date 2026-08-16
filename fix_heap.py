import re
with open("app/build.gradle.kts", "r") as f:
    content = f.read()

# Remove all instances of the injected testOptions
bad_block = """    testOptions {
        unitTests {
            all {
                maxHeapSize = "4g"
            }
        }
    }"""
content = content.replace(bad_block, "")

# Ensure there are no empty blocks or syntax errors left by this
content = re.sub(r"\n\s*\n\s*", "\n\n", content)

# Now just append to android {} properly
content = re.sub(r"android \{", "android {\n    testOptions {\n        unitTests {\n            all {\n                maxHeapSize = \"4g\"\n            }\n        }\n    }", content)

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
