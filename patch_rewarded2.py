with open("app/src/main/java/com/example/RewardedHintAdProvider.kt", "r") as f:
    content = f.read()

content = content.replace('"RECLAMĂ TEST (3s)"', 'activity.getString(R.string.test_ad)')

with open("app/src/main/java/com/example/RewardedHintAdProvider.kt", "w") as f:
    f.write(content)
