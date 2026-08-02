import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

# Add AdManager property
if "lateinit var adManager: AdManager" not in text:
    text = text.replace("private lateinit var billingRepository: BillingRepository", "private lateinit var billingRepository: BillingRepository\n    lateinit var adManager: AdManager\n    lateinit var rewardedHintAdProvider: RewardedHintAdProvider")

# Initialize AdManager in onCreate
if "adManager = AdManager(this)" not in text:
    text = text.replace("billingRepository = BillingRepositoryFactory.create(this, prefs)", "billingRepository = BillingRepositoryFactory.create(this, prefs)\n        adManager = AdManager(this)\n        adManager.initialize(this)\n        rewardedHintAdProvider = AdMobRewardedHintAdProvider(this, adManager)")

# Replace FakeRewardedHintAdProvider with rewardedHintAdProvider in GameScreen usage
text = re.sub(r'FakeRewardedHintAdProvider\(\)\.loadAndShow', 'rewardedHintAdProvider.loadAndShow', text)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
