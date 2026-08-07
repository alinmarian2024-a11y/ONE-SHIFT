package com.example

import android.app.Activity
import android.content.Context
import com.example.R
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface BillingRepository {
    val isAdFree: StateFlow<Boolean>
    val uiState: StateFlow<BillingUiState>
    
    fun initiatePurchaseFlow(activity: Activity)
    fun restorePurchases()
    fun clearUiState()
}

sealed class BillingUiState {
    object Idle : BillingUiState()
    object Loading : BillingUiState()
    data class Success(val message: String) : BillingUiState()
    data class Error(val message: String) : BillingUiState()
    data class PurchaseDialog(
        val title: String, 
        val description: String, 
        val benefits: List<String>,
        val price: String
    ) : BillingUiState()
}

// Verificarea prin backend poate fi adăugată ulterior pentru protecție suplimentară

class PlayBillingRepository(private val context: Context, private val prefs: android.content.SharedPreferences) : BillingRepository, PurchasesUpdatedListener {
    private val _isAdFree = MutableStateFlow(prefs.getBoolean("is_ad_free", false))
    override val isAdFree: StateFlow<Boolean> = _isAdFree.asStateFlow()

    private val _uiState = MutableStateFlow<BillingUiState>(BillingUiState.Idle)
    override val uiState: StateFlow<BillingUiState> = _uiState.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var billingClient: BillingClient
    private var productDetails: ProductDetails? = null

    init {
        val pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()

        
        billingClient = BillingClient.newBuilder(context)

            .setListener(this)
            .enablePendingPurchases(pendingPurchasesParams)
            .build()

        connectToBillingService()
    }
    
    override fun clearUiState() {
        _uiState.value = BillingUiState.Idle
    }

    private fun connectToBillingService() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun queryProductDetails() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("remove_ads_lifetime")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, queryResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val list = queryResult.productDetailsList
                if (list != null && list.isNotEmpty()) {
                    productDetails = list[0]
                }
            }
        }
    }

    private fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
            
        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val isPurchased = purchases.any { purchase ->
                    purchase.products.contains("remove_ads_lifetime") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                updateAdFreeState(isPurchased)
                
                purchases.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        acknowledgePurchase(purchase.purchaseToken)
                    }
                }
            }
        }
    }

    override fun initiatePurchaseFlow(activity: Activity) {
        if (_isAdFree.value) {
            _uiState.value = BillingUiState.Error(context.getString(R.string.purchase_active))
            return
        }

        val details = productDetails
        val benefitsList = listOf(
            context.getString(R.string.benefit_1),
            context.getString(R.string.benefit_2),
            context.getString(R.string.benefit_3),
            context.getString(R.string.benefit_4)
        )
        if (details != null) {
            val price = details.oneTimePurchaseOfferDetails?.formattedPrice ?: "N/A"
            _uiState.value = BillingUiState.PurchaseDialog(
                title = context.getString(R.string.purchase_title),
                description = context.getString(R.string.purchase_desc),
                benefits = benefitsList,
                price = price
            )
            
            // To launch billing flow later from dialog:
            // val productDetailsParamsList = listOf(
            //     BillingFlowParams.ProductDetailsParams.newBuilder()
            //         .setProductDetails(details)
            //         .build()
            // )
            // val billingFlowParams = BillingFlowParams.newBuilder()
            //     .setProductDetailsParamsList(productDetailsParamsList)
            //     .build()
            // billingClient.launchBillingFlow(activity, billingFlowParams)
        } else {
            _uiState.value = BillingUiState.Error(context.getString(R.string.product_unavailable))
        }
    }
    
    fun launchActualPurchaseFlow(activity: Activity) {
        val details = productDetails ?: return
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun restorePurchases() {
        _uiState.value = BillingUiState.Loading
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
            
        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val isPurchased = purchases.any { purchase ->
                    purchase.products.contains("remove_ads_lifetime") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                
                if (isPurchased) {
                    updateAdFreeState(true)
                    _uiState.value = BillingUiState.Success(context.getString(R.string.purchase_restored))
                    
                    purchases.forEach { purchase ->
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                            acknowledgePurchase(purchase.purchaseToken)
                        }
                    }
                } else {
                    _uiState.value = BillingUiState.Error(context.getString(R.string.no_purchase_found))
                }
            } else {
                _uiState.value = BillingUiState.Error(context.getString(R.string.restore_error))
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            _uiState.value = BillingUiState.Error(context.getString(R.string.purchase_cancelled))
        } else {
            _uiState.value = BillingUiState.Error(context.getString(R.string.purchase_error, billingResult.responseCode))
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (purchase.products.contains("remove_ads_lifetime")) {
                updateAdFreeState(true)
                _uiState.value = BillingUiState.Success(context.getString(R.string.ads_removed_success))
            }

            if (!purchase.isAcknowledged) {
                acknowledgePurchase(purchase.purchaseToken)
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            _uiState.value = BillingUiState.Error(context.getString(R.string.purchase_pending))
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
             // handled
        }
    }

    private fun updateAdFreeState(isPurchased: Boolean) {
        _isAdFree.value = isPurchased
        prefs.edit().putBoolean("is_ad_free", isPurchased).apply()
    }
}

class FakeBillingRepository(private val context: Context, private val prefs: android.content.SharedPreferences) : BillingRepository {
    private val _isAdFree = MutableStateFlow(prefs.getBoolean("is_ad_free", false))
    override val isAdFree: StateFlow<Boolean> = _isAdFree.asStateFlow()

    private val _uiState = MutableStateFlow<BillingUiState>(BillingUiState.Idle)
    override val uiState: StateFlow<BillingUiState> = _uiState.asStateFlow()

    override fun clearUiState() {
        _uiState.value = BillingUiState.Idle
    }

    override fun initiatePurchaseFlow(activity: Activity) {
        if (_isAdFree.value) {
            _uiState.value = BillingUiState.Error(context.getString(R.string.purchase_active))
            return
        }
        
        val benefitsList = listOf(
            context.getString(R.string.benefit_1),
            context.getString(R.string.benefit_2),
            context.getString(R.string.benefit_3),
            context.getString(R.string.benefit_4)
        )
        
        _uiState.value = BillingUiState.PurchaseDialog(
            title = context.getString(R.string.purchase_title),
            description = context.getString(R.string.purchase_desc),
            benefits = benefitsList,
            price = context.getString(R.string.test_price)
        )
    }
    
    fun launchActualPurchaseFlow(activity: Activity) {
        _uiState.value = BillingUiState.Loading
        
        activity.runOnUiThread {
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isAdFree.value = true
                prefs.edit().putBoolean("is_ad_free", true).apply()
                _uiState.value = BillingUiState.Success(context.getString(R.string.ads_removed_success))
            }, 1500)
        }
    }

    override fun restorePurchases() {
        _uiState.value = BillingUiState.Loading
        
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            if (prefs.getBoolean("is_ad_free", false)) { // Fake finding it if it was previously bought in fake
                _isAdFree.value = true
                _uiState.value = BillingUiState.Success(context.getString(R.string.purchase_restored))
            } else {
                _uiState.value = BillingUiState.Error(context.getString(R.string.no_purchase_found))
            }
        }, 1000)
    }
}

object BillingRepositoryFactory {
    fun create(context: Context, prefs: android.content.SharedPreferences): BillingRepository {
        return if (BuildConfig.DEBUG) {
            FakeBillingRepository(context, prefs)
        } else {
            PlayBillingRepository(context, prefs)
        }
    }
}

