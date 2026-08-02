import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

attribution_tags = """    <attribution android:tag="game_audio" android:label="@string/app_name" />
    <attribution android:tag="admob" android:label="@string/app_name" />
    <attribution android:tag="billing" android:label="@string/app_name" />
"""

if 'android:tag="admob"' not in text:
    text = text.replace('    <attribution android:tag="game_audio" android:label="@string/app_name" />\n', attribution_tags)

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
