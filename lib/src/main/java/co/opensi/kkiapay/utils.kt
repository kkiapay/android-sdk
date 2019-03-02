package co.opensi.kkiapay

/**
 * @author Shadai ALI ( shadai.ali@opensi.co )
 * created  at 5/17/18.
 */

inline infix fun <reified T : Any> T.merge ( target: Any) {
        TODO( "return this == (target + this)")
}


/**
 * Exception
 * Prevent request without developer API KEY
 */
class kkiaPayNotInitializedException(message: String?) : Exception(message) {

    override val message: String?
        get() = "Before any request you should call KKIAPAY init method with your API key"
}


/**
 * Check if provided phone number
 * is right africa phone number with country
 * code (Except Egypt)
 */
fun is_valid_phone(phone: String): Boolean{
    return  phone.matches("""^2{1}(\d){10,}$""".toRegex())
}