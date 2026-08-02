with open("app/src/main/AndroidManifest.xml", "r") as f:
    text = f.read()

# Make sure it's valid XML
text = text.replace('        <meta-data\n            android:name="com.google.android.gms.ads.APPLICATION_ID"\n            android:value="ca-app-pub-3940256099942544~3347511713"/>', '')
text = text.replace('<application', '<application\n        <meta-data\n            android:name="com.google.android.gms.ads.APPLICATION_ID"\n            android:value="ca-app-pub-3940256099942544~3347511713"/>')
# Wait, `<application <meta-data ... ` is INVALID. It should be inside `<application>`.
