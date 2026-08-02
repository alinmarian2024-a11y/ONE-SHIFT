import re
with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

text = text.replace("    <uses-permission android:name=\"android.permission.INTERNET\" />", "    <uses-permission android:name=\"android.permission.INTERNET\" />\n    <attribution android:tag=\"@string/empty\" android:label=\"@string/app_name\" />")

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
