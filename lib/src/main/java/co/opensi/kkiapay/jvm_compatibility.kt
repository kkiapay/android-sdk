package co.opensi.kkiapay

/**
 * Created by geecko on 6/4/18.
 */
interface KKiapayCallback {

    fun onResponse(status: STATUS, phone: String, transactionId: String)
}