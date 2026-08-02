import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

text = text.replace('    <attribution android:tag="" android:label="@string/app_name" />\n', '')

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
