import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

if 'android:tag="null"' not in text:
    text = text.replace('<application', '    <attribution android:tag="null" android:label="@string/app_name" />\n    <application')

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
