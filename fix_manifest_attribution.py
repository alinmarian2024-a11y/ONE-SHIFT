import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

attribution_tag = '    <attribution android:tag="game_audio" android:label="@string/app_name" />\n'
if 'android:tag="game_audio"' not in text:
    text = text.replace('<application', attribution_tag + '\n    <application')

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
