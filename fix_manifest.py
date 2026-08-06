with open('app/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()

meta_tag = '\n        <meta-data\n            android:name="com.google.android.gms.ads.APPLICATION_ID"\n            android:value="ca-app-pub-3940256099942544~3347511713"/>\n'

if 'com.google.android.gms.ads.APPLICATION_ID' not in content:
    content = content.replace('<application', '<application' + meta_tag)

with open('app/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)
