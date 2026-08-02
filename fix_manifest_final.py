import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

# Fix the invalid tag
invalid_tag = '\n        <meta-data\n            android:name="com.google.android.gms.ads.APPLICATION_ID"\n            android:value="ca-app-pub-3940256099942544~3347511713"/>\n'

if '<application' + invalid_tag in text:
    text = text.replace('<application' + invalid_tag, '<application')

# Now inject it safely after <application ...>
if "com.google.android.gms.ads.APPLICATION_ID" not in text:
    # Find the closing angle bracket of the <application ...> tag
    text = re.sub(r'(<application[^>]+>)', r'\1\n        <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="ca-app-pub-3940256099942544~3347511713"/>', text)

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(text)
