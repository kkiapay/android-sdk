package co.opensi.kkiapay_android_sdk


/**
 * ALL THINGS IN THIS FILE IS PUBLIC DATA
 */

internal const val END_POINT = "https://api.kkiapay.me"

internal const val API_ENDPOINT = "$END_POINT/api/v1"

internal const val API_KEY_HEADER_TAG = "x-api-key"
internal const val CONTENT_TYPE = "Content-Type"
internal const val JSON_APPLICATION = "application/json"

internal const val NEW_PAYMENT_ENDPOINT = "payments/request"

internal const val CLAIM_NEW_CHANNEL_ENDPOINT="utils/claimchannel"

internal const val IS_INSUFFICIENT_FUND = "insufficent_fund"

//legacy
internal var PUBLIC_API_KEY = ""

enum class STATUS {
    SUCCESS,
    FAILED,
    INSUFFICIENT_FUND,
    PENDING,
    INVALID_PHONE_NUMBER,
    INVALID_API_KEY,
    TRANSACTION_NOT_FOUND,
    INVALID_TRANSACTION,
    UNKNOWN
}