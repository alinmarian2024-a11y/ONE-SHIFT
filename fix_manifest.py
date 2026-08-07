with open("app/src/main/AndroidManifest.xml", "r") as f:
    content = f.read()

if "firebase_analytics_collection_enabled" not in content:
    content = content.replace("</application>", '    <meta-data android:name="firebase_analytics_collection_enabled" android:value="false" />\n    </application>')

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(content)
