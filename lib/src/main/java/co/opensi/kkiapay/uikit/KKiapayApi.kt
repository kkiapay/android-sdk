package co.opensi.kkiapay.uikit

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.requests.upload
import com.github.kittinunf.fuel.util.FuelRouting
import org.json.JSONObject
import java.io.File

/**
 * @author Armel FAGBEDJI ( armel.fagbedji@opensi.co )
 * created  at 01/03/2019
 */

internal sealed class KKiapayApi : FuelRouting {
    override val basePath: String
        get() = "https://api.kkiapay.me"

    override val headers: Map<String, HeaderValues>?
        get() = mapOf("x-api-key" to listOf(apiKei))

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
        internal lateinit var apiKei: String

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