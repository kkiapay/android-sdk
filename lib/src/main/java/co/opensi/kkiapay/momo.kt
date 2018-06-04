package co.opensi.kkiapay

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson




/**
 * @author Shadai ALI ( shadai.ali@opensi.co )
 * created  at 14/05/2018
 */

val MTN_MOMO_PAYMENTS_BACK =  "payment_back"

private var instance: KkiaPay? = null

class KkiaPay( val api_key: String) {


    init {
        init(api_key)
    }

    /**
     *
     */
     private var sdkIsInitailized: Boolean = false
     private var paymentRequest: PaymentRequest? = null
     private val pending_transactions = mutableMapOf<String,Any>()


    /**
     * Initialize SDK with API key.
     * This function didn't check your API keys but keep it for future requests
     *  @param String  developer prod / test API keys check https://kkiapay.me
     *  @return KkiaPay
     */
     private fun init(api_key: String): KkiaPay {

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
     * @param Subscriber
     * @return KkiaPay
     */
    fun to(subscriber: Subscriber): KkiaPay {
        if(!sdkIsInitailized) throw kkiaPayNotInitializedException("")
        paymentRequest = PaymentRequest(subscriber.firstName, subscriber.lastName, subscriber.phoneNumber)
        return this
    }


    /**
     * Setup clients informations ...
     * @param String
     * @return KkiaPay
     */
    fun to(phoneNumber: String): KkiaPay {
        if(!sdkIsInitailized) throw kkiaPayNotInitializedException("")
        paymentRequest = PaymentRequest(phoneNumber = phoneNumber)
        return this
    }


    /**
     * process the payment
     */
    fun take(amount: Int,cb : (STATUS,String) -> Unit) {

        paymentRequest?.let {
            it.amount = amount

            request_payement(it,cb)
        } ?: throw Exception("Kkiapay.debit() should be called before request")

    }

    /**
     * utility for jvm compatibility
     */
    fun take(amount: Int, cb: KKiapayCallback ){
        take(amount,cb)
    }
}



fun handle_payment_status(transactionId: String,cb: (String) -> Unit){
    subscribe(transactionId, MTN_MOMO_PAYMENTS_BACK, {
        cb(it)
    })
}


/**
 * @hide
 * Internal function
 */
private fun request_payement(paymentRequest:PaymentRequest ,cb: (STATUS, String) -> Unit) {

    NEW_PAYMENT_ENDPOINT.httpPost().body(paymentRequest.json())
            .responseString { _, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val error = Gson().fromJson<Error>(String(response.data),Error::class.java)
                        when(error.status) {
                            4001 -> cb(STATUS.INVALID_PHONE_NUMBER,paymentRequest.phoneNumber)
                            4003 -> cb(STATUS.INVALID_API_KEY,paymentRequest.phoneNumber)
                            else -> cb(STATUS.FAILED,paymentRequest.phoneNumber)
                        }
                    }

                    is Result.Success -> {
                        val transaction = Gson().fromJson<Transaction>(result.get(),Transaction::class.java)

                        handle_payment_status(transaction.internalTransactionId,{

                            val paymentStatus = Gson().fromJson<PaymentStatus>(it,PaymentStatus::class.java)
                            when(paymentStatus.isPaymentSuccess){
                                true  ->  cb(STATUS.SUCCESS,paymentStatus.account)
                                false -> when(paymentStatus.failureCode) {
                                    IS_INSUFFICIENT_FUND -> cb(STATUS.INSUFFICIENT_FUND,paymentStatus.account)
                                    else -> cb(STATUS.FAILED,paymentStatus.account)
                                }
                            }
                        })

                    }
                }

            }
}



infix fun String.debit(amount : Int): ((STATUS, String) -> Unit) -> Unit {

    return {
        cb: (STATUS,String) -> Unit ->
        request_payement(PaymentRequest(amount=amount,phoneNumber = this),cb)
    }
}



private data class Transaction (val transactionId:String, val internalTransactionId: String, val status: String)

private data class PaymentStatus(val transactionId:String, val status: String, val isPaymentSuccess: Boolean,
                                 val failureCode: String, val failureMessage: String, val account: String)

private data class Error(val status: Int,val reason: String)

private data class PaymentRequest(val firstname: String ="", val lastname: String="", val phoneNumber: String,
                                  var amount: Int = 0, var transactionId: String = "") {

    fun json() = Gson().toJson(this).apply {
        this.replace("phone_number", "phoneNumber")
    }
}

data class Subscriber(val phoneNumber: String, val firstName: String,val lastName: String){
    fun debit(amount: Int,cb : (STATUS,String) -> Unit ) = request_payement(
            PaymentRequest(firstName,lastName,phoneNumber,amount),cb
    )
}
//Redundant due to kotlin data class non inheritance behaviour

 class  SubscriberBuilder {
    private val values = mutableMapOf<String,Any>()
    var phoneNumber: String by values
    var firstName: String by values
    var lastName: String by values

    fun build() = Subscriber(phoneNumber, firstName, lastName)

}


fun from(builder: SubscriberBuilder.() -> Unit) : Subscriber = SubscriberBuilder().apply(builder).build()
