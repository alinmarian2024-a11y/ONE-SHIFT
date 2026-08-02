import re

with open("app/src/main/java/com/example/BillingRepository.kt", "r") as f:
    text = f.read()

text = text.replace('class FakeBillingRepository(private val prefs: android.content.SharedPreferences) : BillingRepository {',
                    'class FakeBillingRepository(private val context: android.content.Context, private val prefs: android.content.SharedPreferences) : BillingRepository {')
text = text.replace('return FakeBillingRepository(prefs)',
                    'return FakeBillingRepository(context, prefs)')

with open("app/src/main/java/com/example/BillingRepository.kt", "w") as f:
    f.write(text)
