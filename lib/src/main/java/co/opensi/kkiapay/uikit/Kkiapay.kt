package co.opensi.kkiapay.uikit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_REQUEST_CODE
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_URL
import co.opensi.kkiapay_android_sdk.STATUS
import com.google.gson.Gson
import org.apache.commons.codec.binary.Base64


object Kkiapay {
    private lateinit var me: Me

    /**
     * Initialize SDK.
     * This function didn't check your API keys but keep it for future requests
     *  @param apiKey  developer prod / test API keys check https://kkiapay.me
     *  @param context  Application Context
     *  @param sdkConfig  SDK configurations. It is optional
     */
    @JvmOverloads
    @JvmStatic
    fun init(
        context: Context,
        apiKey: String,
        sdkConfig: SdkConfig = SdkConfig()
    ) {
        me = Me(context, apiKey, sdkConfig)
    }

    /**
     * Recover the Kkiapay instance to perform actions
     * @return [Me] Instance of Kkiapay
     * @exception [IllegalAccessException] If the SDK was not initialized before use
     */
    @JvmStatic
    fun get(): Me {
        if (!Kkiapay::me.isInitialized)
            throw IllegalAccessException(
                "You must initialise Kkiapay SDK in the onCreate methode of your App's" +
                        " Application first"
            )
        return me
    }
}

class Me internal constructor(
    context: Context,
    private val apiKey: String,
    private val sdkConfig: SdkConfig
) {

    private var requestPaymentAction: RequestPaymentAction? = null
    private var sdkListener: ((STATUS, String?) -> Unit)? = null

    /**
     * MomoPay Instance
     */

    init {
        sdkConfig.run {
            convertColorToString(context)
            //convertImageResToImageUrl(context)
        }
    }

    /**
     * Make a payment request
     * @param activity Payment activity
     * @param amount Amount to be paid
     * @param reason Payment description
     * @param name The name of the client
     * @param phone The customer's phone number
     * @param sandbox Make request on sandbox
     * @param data The callback redirect data
     * @param customApiKey  developer prod / test API keys check https://kkiapay.me
     * @exception [IllegalAccessException] If a listener was not configured on the UI-SDK
     */
    @JvmOverloads
    fun requestPayment(
        activity: AppCompatActivity,
        amount: Int,
        name: String,
        phone: String,
        api_key: String,
        sandbox: Boolean,
        email: String = "",
        reason: String = "",
        partnerId: String = "",
        data: String = "",
        countries: List<String> = listOf("BJ"),
        paymentMethods: List<String> = listOf("momo", "card", "direct_debit"),
        callback: String = KKIAPAY_REDIRECT_URL,
    ) {
        sdkListener ?: throw IllegalAccessException(
            "Sdk Listener is null, you must setup one before the call of" +
                    " \"requestPayment\" methode"
        )

        sdkConfig.enableSandbox = sandbox
        KKIAPAY_REDIRECT_URL = callback
        val user = User(
            amount = amount,
            reason = reason,
            email = email,
            fullname = name,
            key = api_key,
            callback = callback,
            phoneNumber = phone,
            sandbox = sandbox,
            data = data,
            countries = countries,
            partnerId = partnerId,
            paymentMethods = paymentMethods,
        )
        requestPaymentAction = RequestPaymentAction(user)
        requestPaymentAction?.invoke(activity, sdkConfig)
    }

    /**
     * Configure a listener for the UI-SDK, to manage the information returned by the latter
     * @param listener mandatory
     */
    fun setListener(listener: (STATUS, String?) -> Unit): Me {
        sdkListener = listener
        return this
    }

    /**
     * Remove UI-SDK listener
     */
    fun removeSdkListener() {
        sdkListener = null
    }

    /**
     * To call in onActivityResult of your activity to manage the return response
     * the UI-SDK using the [sdkListener] which was initially informed
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == KKIAPAY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.run {
                if (hasExtra(KKIAPAY_TRANSACTION_ID)) {
                    val transactionId = getStringExtra(KKIAPAY_TRANSACTION_ID)
                    transactionId?.let {
                        kotlin.run {
                            sdkListener?.invoke(STATUS.SUCCESS, it)
                        }
                    } ?: kotlin.run {
                        sdkListener?.invoke(STATUS.SUCCESS, null)
                    }
                } else
                    sdkListener?.invoke(STATUS.SUCCESS, null)
            } ?: let {
                sdkListener?.invoke(STATUS.SUCCESS, null)
            }
        } else {
            sdkListener?.invoke(STATUS.FAILED, null)
        }
    }

    companion object {
        internal const val KKIAPAY_URL = "https://widget-v3.kkiapay.me"
        internal var KKIAPAY_REDIRECT_URL = "http://redirect.kkiapay.me"
        const val KKIAPAY_REQUEST_CODE = 0xABC
        internal const val KKIAPAY_TRANSACTION_ID = "me.kkiapay.uikit.KKIAPAY_TRANSACTION_ID"
    }
}

/**
 * Configure and customize the UI-SDK.
 * Possibility to configure the color [themeColor]
 * and the shop logo [imageResource]
 */
data class SdkConfig(
    @ColorRes internal val themeColor: Int = -1,
    var enableSandbox: Boolean = false
) {
    internal var imageUrl: String = ""
    internal var color: String = ""

    internal fun convertColorToString(context: Context) {
        if (themeColor != -1) {
            color = String.format("#%06x", ContextCompat.getColor(context, themeColor) and 0xffffff)
            Log.i("Kkiapay.me", color)
        }
    }

   /*internal fun convertImageResToImageUrl(context: Context) {
        if (imageResource != -1) {
            val stream = context.resources.openRawResource(imageResource)
            var bitmap = BitmapFactory.decodeStream(stream)
            bitmap = reduceBitmap(bitmap)
            val file = File(context.cacheDir, "${UUID.randomUUID()}.png")
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
                it.flush()
            }
            KKiapayApi.uploadFile(file)
                ?.responseString { _, _, result ->
                    result.fold({ resutlString ->
                        val uploadKey = Gson().fromJson<List<Map<String, String>>>(
                            resutlString,
                            List::class.java
                        )
                            .first()["fd"]?.trim()
                        imageUrl = KKiapayApi.getUploadedFile(uploadKey)?.url?.toString() ?: ""
                    }) {
                        Log.e("Kkiapay.me", it.toString())
                    }
                }
        }
    }

    private fun reduceBitmap(original: Bitmap): Bitmap {
        val out = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.WEBP, 100, out)
        return BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
    }

    */
}

internal class RequestPaymentAction(private val user: User) {

    operator fun invoke(activity: AppCompatActivity, sdkConfig: SdkConfig) {
        Log.d("TAG", user.toJson())
        Log.d("TAG", "$KKIAPAY_URL/?${user.toBase64(activity.applicationContext, sdkConfig)}")
        activity.startActivityForResult(
            Intent(activity, CustomTabActivity::class.java).apply {
                putExtra(
                    CustomTabActivity.EXTRA_URL,
                    "$KKIAPAY_URL/?${user.toBase64(activity.applicationContext, sdkConfig)}"
                )
                putExtra(
                    CustomTabActivity.EXTRA_THEME,
                    sdkConfig.themeColor
                )
            }, KKIAPAY_REQUEST_CODE
        )
    }
}

internal data class User(
    val amount: Int = 1,
    val reason: String = "",
    val fullname: String = "",
    val key: String = "",
    val callback: String,
    val phoneNumber: String = "",
    val email: String = "",
    val sdk: String = "android",
    val theme: String = "",
    val url: String = "",
    val sandbox: Boolean,
    val host: String? = "",
    val data: String = "",
    val serviceId: String = "INTEGRATION",
    val partnerId: String = "",
    val countries: List<String> = listOf("BJ", "CI"),
    val paymentMethods: List<String> = listOf("momo", "card", "direct_debit")
) {
    fun toBase64(context: Context, sdkConfig: SdkConfig): String {
        val preConvertion = this.copy(
            theme = sdkConfig.color,
            url = sdkConfig.imageUrl,
            host = context.applicationContext.packageName
        )
        val userJson = Gson().toJson(preConvertion).toString()
        return String(Base64.encodeBase64(userJson.toByteArray()))
    }

    fun toJson() = Gson().toJson(this)
}