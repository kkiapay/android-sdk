package co.opensi.kkiapay

/**
 *  @author Shadai ALI ( shadai.ali@opensi.co )
 *  created  at 6/4/18.
 */
interface KKiapayCallback {
    fun onResponse(status: STATUS, phone: String, transactionId: String)
}