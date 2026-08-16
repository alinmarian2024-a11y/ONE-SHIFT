import re
with open("app/build.gradle.kts", "r") as f:
    content = f.read()

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

content += "\ntasks.withType<Test> {\n    maxHeapSize = \"4g\"\n}\n"

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
