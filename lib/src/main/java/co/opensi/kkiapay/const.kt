package co.opensi.kkiapay


/**
 * ALL THINGS IN THIS FILE IS PUBLIC DATA
 */


val END_POINT = "https://api.kkiapay.me"

val API_ENDPOINT = "$END_POINT/api/v1"

val API_KEY_HEADER_TAG = "x-api-key"
val CONTENT_TYPE = "Content-Type"
val JSON_APPLICATION = "application/json"

val NEW_PAYMENT_ENDPOINT = "payments/request"

val IS_INSUFFICIENT_FUND = "insufficent_fund"

val PUBLIC_API_KEY = "fd90fb08dd97f5db5f16"

enum class STATUS {
    SUCCESS,FAILED,INSUFFICIENT_FUND ,PENDING,INVALID_PHONE_NUMBER, INVALID_API_KEY
}