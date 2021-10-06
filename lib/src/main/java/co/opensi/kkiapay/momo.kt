package co.opensi.kkiapay

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson


/**
 * @author Shadai ALI ( shadai.ali@opensi.co )
 * created  at 14/05/2018
 */

const val MTN_MOMO_PAYMENTS_BACK =  "payment_back"

private var instance: MomoPay? = null

class MomoPay internal constructor(api_key: String) {

    init {
        init(api_key)
    }

    /**
     *
     */
     private var sdkIsInitailized: Boolean = false
     private var paymentRequest: PaymentRequest? = null


    /**
     * Initialize SDK with API key.
     * This function didn't check your API keys but keep it for future requests
     *  @param api_key  developer prod / test API keys check https://kkiapay.me
     *  @return MomoPay
     */
     private fun init(api_key: String): MomoPay {

        PUBLIC_API_KEY = api_key

        if (!sdkIsInitailized) {
            FuelManager.instance.baseHeaders = mapOf(API_KEY_HEADER_TAG to api_key,
                    CONTENT_TYPE to JSON_APPLICATION)
            FuelManager.instance.basePath = API_ENDPOINT
            sdkIsInitailized = true
        }

        instance = this

        return this
    }


    /**
     * Setup clients informations ...
     * @param subscriber
     * @return MomoPay
     */
    fun from(subscriber: Subscriber): MomoPay {
        if(!sdkIsInitailized) throw kkiaPayNotInitializedException("")
        paymentRequest = PaymentRequest(subscriber.firstName, subscriber.lastName, subscriber.phoneNumber,stateData = subscriber.data)
        return this
    }


    /**
     * Setup clients informations ...
     * @param phoneNumber
     * @param data payment data object for webhook https://docs.kkiapay.me/v1/tableau-de-bord/webhook
     * @return MomoPay
     */
    fun from(phoneNumber: String, data: String = ""): MomoPay {
        if(!sdkIsInitailized) throw kkiaPayNotInitializedException("")
        paymentRequest = PaymentRequest(phoneNumber = phoneNumber, stateData = data)
        return this
    }


    /**
     * process the payment
     *  [amount]  String -- amount to debit
     *  [cb]
     */
    @Deprecated("Use requestPayment instead")
    private fun _take(amount: Int,cb : (STATUS,String,String) -> Unit) {

        paymentRequest?.let {
            it.amount = amount

            request_payement(it,cb)
        } ?: throw Exception("Kkiapay.debit() should be called before request")

    }

    /**
     * utility for jvm compatibility
     */
    fun debit(amount: Int, cb: KKiapayCallback ){
        _take(amount) { status: STATUS, phone: String, transactionId: String -> cb.onResponse(status,phone, transactionId) }
    }
}

/**
 * @hide
 * Internal function
 */
@Deprecated("Use requestPayment instead")
private fun request_payement(paymentRequest:PaymentRequest ,cb: (STATUS, String, String) -> Unit) {

    claim_channel { channel, error ->

        if(error.trim().isNotEmpty()) throw Exception(error)

        subscribe(channel, MTN_MOMO_PAYMENTS_BACK,{
            //server is connected
            paymentRequest.contact = channel
            NEW_PAYMENT_ENDPOINT.httpPost().body(paymentRequest.json())
                    .responseString { _, response, result ->
                        Log.e("KKIAPAY:","paymentRequestResponse")
                        when (result) {
                            is Result.Failure -> {
                                val theError = Gson().fromJson<Error>(String(response.data),Error::class.java)
                                if (theError == null) {
                                    cb(STATUS.FAILED,paymentRequest.phoneNumber,paymentRequest.transactionId)
                                }else {
                                    when(theError.status) {
                                        4001 -> cb(STATUS.INVALID_PHONE_NUMBER,paymentRequest.phoneNumber,paymentRequest.transactionId)
                                        4003 -> cb(STATUS.INVALID_API_KEY,paymentRequest.phoneNumber,paymentRequest.transactionId)
                                        else -> cb(STATUS.FAILED,paymentRequest.phoneNumber,paymentRequest.transactionId)
                                    }
                                }
                            }

                            is Result.Success -> {
                                val transaction = Gson().fromJson<Transaction>(result.get(),Transaction::class.java)
                                //inform user that request is pending
                            }
                        }

                    }

        },{
            //server respond after transaction
                val paymentStatus = Gson().fromJson<PaymentStatus>(it,PaymentStatus::class.java)
                when(paymentStatus.isPaymentSucces){
                    true  ->  runOnUiThread { cb(STATUS.SUCCESS,paymentStatus.account,paymentStatus.transactionId) }
                    false -> runOnUiThread { cb(STATUS.FAILED,paymentStatus.account,paymentStatus.transactionId) }
// I'm comment this line because new response model not contain "paymentStatus.failureCode"
//                    false -> when(paymentStatus.failureCode) {
//                        IS_INSUFFICIENT_FUND -> runOnUiThread { cb(STATUS.INSUFFICIENT_FUND,paymentStatus.account,paymentStatus.transactionId)  }
//                        else -> runOnUiThread { cb(STATUS.FAILED,paymentStatus.account,paymentStatus.transactionId) }
//                    }
                }

        })
    }


}

internal fun runOnUiThread(cb: () -> Unit){
    Handler(Looper.getMainLooper()).post { cb() }
}


@Deprecated("Use requestPayment instead")
infix fun String.debit(amount : Int): ((STATUS, String, String) -> Unit) -> Unit {

    return {
        cb: (STATUS,String,String) -> Unit ->
        request_payement(PaymentRequest(amount=amount,phoneNumber = this),cb)
    }
}



internal data class Transaction (val transactionId:String,
                                val internalTransactionId: String,
                                val status: String)

private data class PaymentStatus(val transactionId:String, val status: String, val isPaymentSucces: Boolean,
                                 val failureCode: String, val failureMessage: String, val account: String)

internal data class Error(val status: Int, val reason: String)

private data class PaymentRequest(
        val firstname: String ="",
        val lastname: String="",
        val phoneNumber: String,
        var amount: Int = 0,
        var transactionId: String = "",
        var contact: String = "",
        var stateData: String = "" ) {

    fun json() = Gson().toJson(this).apply {
        this.replace("phone_number", "phoneNumber")
    }
}

data class Subscriber(val phoneNumber: String, val firstName: String,val lastName: String, val data: String)

/**
 * @param phoneNumber payment phone number
 * @param firstName payment user first name
 * @param lastName payment user last name
 * @param data payment data object for webhook https://docs.kkiapay.me/v1/tableau-de-bord/webhook
 */
 class  SubscriberBuilder {
    private val values = mutableMapOf<String,Any>()
    var phoneNumber: String by values
    var firstName: String by values
    var lastName: String by values
    var data: String by values

    fun build() = Subscriber(phoneNumber, firstName, lastName, data)

}

fun from(builder: SubscriberBuilder.() -> Unit) : Subscriber = SubscriberBuilder().apply(builder).build()
