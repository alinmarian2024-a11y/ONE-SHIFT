import re
with open("app/src/main/res/values/strings.xml", "r") as f:
    text = f.read()

text = text.replace("</resources>", "    <string name=\"empty\"></string>\n</resources>")

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(text)
