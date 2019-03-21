package co.opensi.kkiapay.uikit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.opensi.kkiapay.Error
import co.opensi.kkiapay.MomoPay
import co.opensi.kkiapay.STATUS
import co.opensi.kkiapay.Transaction
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_REQUEST_CODE
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_URL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.requests.upload
import com.github.kittinunf.fuel.util.FuelRouting
import com.google.gson.Gson
import org.apache.commons.codec.binary.Base64
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * @author Armel FAGBEDJI ( armel.fagbedji@opensi.co )
 * created  at 01/03/2019
 */

object Kkiapay{
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
    fun init(context: Context,
             apiKey:String,
             sdkConfig: SdkConfig = SdkConfig()) {
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
            throw IllegalAccessException("You must initialise Kkiapay SDK in the onCreate methode of your App's" +
                    " Application first")
        return me
    }
}

class Me internal constructor(context: Context, private val apiKey: String, private val sdkConfig: SdkConfig) {

    private var requestPaymentAction: RequestPaymentAction? = null
    private var sdkListener: ((STATUS, String?) -> Unit)? = null

    /**
     * MomoPay Instance
     */
    val momoPay: MomoPay

    init {
        KKiapayApi.apiKey = apiKey
        sdkConfig.run {
            convertColorToString(context)
            convertImageResToImageUrl(context)
        }
        momoPay = MomoPay(apiKey)
    }

    /**
     * Make a payment request
     * @param activity Payment activity
     * @param amount Amount to be paid
     * @param reason Payment description
     * @param name The name of the client
     * @param phone The customer's phone number
     * @exception [IllegalAccessException] If a listener was not configured on the UI-SDK
     */
    @JvmOverloads
    fun requestPayment(activity: AppCompatActivity,
                       amount: String,
                       reason: String,
                       name: String,
                       phone: String = ""){
        sdkListener ?: throw IllegalAccessException("Sdk Listener is null, you must setup one before the call of" +
                " \"requestPayment\" methode")

        val user = User(amount, reason, name, apiKey, KKIAPAY_REDIRECT_URL, phone = phone)
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
    fun removeSdkListener(){
        sdkListener = null
    }

    /**
     * To call in onActivityResult of your activity to manage the return response
     * the UI-SDK using the [sdkListener] which was initially informed
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == KKIAPAY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            data?.run {
                if (hasExtra(KKIAPAY_TRANSACTION_ID)){
                    val transactionId = getStringExtra(KKIAPAY_TRANSACTION_ID)
                    transactionId?.let {
                        KKiapayApi.checkTransactionStatus(it)
                                .responseString { _, _, result ->
                                    result.fold({ resutlString ->
                                        val transaction = Gson().fromJson<Transaction>(resutlString, Transaction::class.java)
                                        sdkListener?.invoke(when(transaction.status){
                                            "SUCCESS" -> STATUS.SUCCESS
                                            "INVALID_TRANSACTION" -> STATUS.INVALID_TRANSACTION
                                            "TRANSACTION_NOT_FOUND" -> STATUS.TRANSACTION_NOT_FOUND
                                            "FAILED" -> STATUS.FAILED
                                            else -> STATUS.UNKNOWN
                                        },
                                                transaction.transactionId)
                                    }){fuelError ->
                                        Log.i("Kkiapay.me", fuelError.toString())
                                        val theError = Gson().fromJson<Error>(String(fuelError.errorData), Error::class.java)
                                        theError?.let {error ->
                                            sdkListener?.invoke(when(error.status) {
                                                4001 -> STATUS.INVALID_PHONE_NUMBER
                                                4003 -> STATUS.INVALID_API_KEY
                                                else -> STATUS.FAILED
                                            }, null)
                                        } ?: kotlin.run {
                                            sdkListener?.invoke(STATUS.FAILED, null)
                                        }
                                    }
                                }
                    } ?: kotlin.run {
                        sdkListener?.invoke(STATUS.SUCCESS, null)
                    }
                } else
                    sdkListener?.invoke(STATUS.SUCCESS, null)
            } ?: let {
                sdkListener?.invoke(STATUS.SUCCESS, null)
            }
        } else{
            sdkListener?.invoke(STATUS.FAILED, null)
        }
    }

    companion object {
        internal const val KKIAPAY_URL = "https://widget.kkiapay.me"
        internal const val KKIAPAY_REDIRECT_URL = "http://redirect.kkiapay.me"
        const val KKIAPAY_REQUEST_CODE = 0xABC
        internal const val KKIAPAY_TRANSACTION_ID = "me.kkiapay.uikit.KKIAPAY_TRANSACTION_ID"
    }
}

/**
 * Configure and customize the UI-SDK.
 * Possibility to configure the color [themeColor]
 * and the shop logo [imageResource]
 */
data class SdkConfig(@RawRes private val imageResource: Int = -1, @ColorRes private val themeColor: Int = -1){
    internal var imageUrl: String = ""
    internal var color: String = ""

    internal fun convertColorToString(context: Context){
        if (themeColor != -1){
            color = String.format("#%06x", ContextCompat.getColor(context, themeColor) and 0xffffff)
            Log.i("Kkiapay.me", color)
        }
    }

    internal fun convertImageResToImageUrl(context: Context){
        if (imageResource != -1){
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
                        result.fold({resutlString ->
                            val uploadKey = Gson().fromJson<List<Map<String, String>>>(resutlString, List::class.java)
                                    .first()["fd"]?.trim()
                            imageUrl = KKiapayApi.getUploadedFile(uploadKey)?.url?.toString() ?: ""
                        }){
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
}

internal class RequestPaymentAction(private val user: User) {

    operator fun invoke(activity: AppCompatActivity, sdkConfig: SdkConfig){
        activity.startActivityForResult(
                Intent(activity, CustomTabActivity::class.java).apply {
                    putExtra(CustomTabActivity.EXTRA_URL,
                            "$KKIAPAY_URL/?=${user.toBase64(sdkConfig)}")
                }, KKIAPAY_REQUEST_CODE)
    }
}

internal data class User(val amount: String = "",
                         val reason: String = "",
                         val name: String = "",
                         val key: String = "",
                         val callback: String = "",
                         val phone: String = "",
                         val sdk: String = "android",
                         val theme: String = "",
                         val url: String = "") {
    fun toBase64(sdkConfig: SdkConfig) : String{
        val preConvertion = this.copy(theme = sdkConfig.color, url = sdkConfig.imageUrl)
        val userJson = Gson().toJson(preConvertion).toString()
        return String(Base64.encodeBase64(userJson.toByteArray()))
    }
}




private sealed class KKiapayApi : FuelRouting {
    override val basePath: String
        get() = "https://api.kkiapay.me"

    override val headers: Map<String, HeaderValues>?
        get() = mapOf("x-api-key" to listOf(apiKey))

    override val method: Method
        get() = Method.POST

    override val body: String?
        get() = null

    override val bytes: ByteArray?
        get() = null

    override val params: Parameters?
        get() = emptyList()

    private class UploadFile(private val classification: String = "android_client_store_icon"): KKiapayApi(){
        override val path: String
            get() = "/utils/upload"

        override val params: List<Pair<String, Any?>>?
            get() = listOf("type" to classification)
    }

    private class CheckTansactionStatus(private val transactionId: String): KKiapayApi() {
        override val path: String
            get() = "/api/v1/transactions/status"

        override val headers: Map<String, HeaderValues>?
            get() = super.headers?.plus("Content-Type" to listOf("application/json"))

        override val body: String?
            get() = JSONObject().putOpt("transactionId", transactionId).toString()
    }

    private class GetUploadedFile(private val fileKey: String): KKiapayApi() {
        override val path: String
            get() = "/utils/file/$fileKey"

        override val method: Method
            get() = Method.GET
    }

    companion object {
        internal lateinit var apiKey: String

        internal fun uploadFile(file: File?) =
                file?.run { Fuel.request(UploadFile())
                        .upload()
                        .add { FileDataPart(this, name = "file") } }

        internal fun checkTransactionStatus(transactionId: String) =
                Fuel.request(CheckTansactionStatus(transactionId))

        internal fun getUploadedFile(fileKey: String?) =
                fileKey?.run{
                    Fuel.request(GetUploadedFile(fileKey))
                }
    }
}