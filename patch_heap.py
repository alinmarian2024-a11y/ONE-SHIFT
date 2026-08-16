import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

if "maxHeapSize =" not in content:
    replacement = """    testOptions {
        unitTests {
            all {
                maxHeapSize = "4g"
            }
        }
    }
}"""
    # replace the very last closing brace of android block
    content = re.sub(r"\}\s*$", replacement, content, flags=re.MULTILINE)
    with open("app/build.gradle.kts", "w") as f:
        f.write(content)
