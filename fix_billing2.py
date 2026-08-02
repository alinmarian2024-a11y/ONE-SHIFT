import re
import os

with open("app/src/main/java/com/example/BillingRepository.kt", "r") as f:
    text = f.read()

fix_code = """
        val attributedContext = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            context.createAttributionContext("billing")
        } else {
            context
        }
        billingClient = BillingClient.newBuilder(attributedContext)
"""

if "attributedContext" not in text:
    text = text.replace("        billingClient = BillingClient.newBuilder(context)", fix_code)
    
with open("app/src/main/java/com/example/BillingRepository.kt", "w") as f:
    f.write(text)
