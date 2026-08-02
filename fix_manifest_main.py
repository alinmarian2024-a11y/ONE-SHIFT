import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

attribution_tags = """    <attribution android:tag="main" android:label="@string/app_name" />
"""

if 'android:tag="main"' not in text:
    text = text.replace('    <attribution android:tag="admob" android:label="@string/app_name" />\n', '    <attribution android:tag="admob" android:label="@string/app_name" />\n' + attribution_tags)

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
