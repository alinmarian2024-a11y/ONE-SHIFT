import re
import os

if os.path.exists("app/src/main/java/com/example/BillingRepository.kt"):
    with open("app/src/main/java/com/example/BillingRepository.kt", "r") as f:
        text = f.read()
    
    fix_code = """
    private val attributedContext: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        context.createAttributionContext("billing")
    } else {
        context
    }
"""

    if "attributedContext" not in text:
        text = text.replace("    private val billingClient = BillingClient.newBuilder(context)", fix_code + "    private val billingClient = BillingClient.newBuilder(attributedContext)")
    
    with open("app/src/main/java/com/example/BillingRepository.kt", "w") as f:
        f.write(text)
