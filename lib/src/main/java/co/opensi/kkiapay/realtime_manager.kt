package co.opensi.kkiapay

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.socket.client.IO
import io.socket.client.Socket

/**
 * @author Shadai ALI ( shadai.ali@opensi.co )
 * created  at 5/23/18.
 */


/**
 * Some treatment in the KKIAPAY API are delayed.
 * While a streaming layer above the REST is needed
 * to listen notifications . More details at https://docs.kkiapay.com/v1/websocket.
 * realtime_manager exposes functions for subscribe to API notifications
 */

lateinit var options: IO.Options
lateinit var socket: Socket
var initialized = false
val TAG = "KKIAPAY SOCKET"

fun init() {

    options = IO.Options()
    options.path = "/websocket"
    initialized = true

    options.transports = arrayOf("websocket")
    options.forceNew = true
    options.reconnection = true
}

/**
 * subscribe to event from server
 * events are grouped by room
 * you should be registered in right room before
 * listeen for specific event broadcast
 *
 * @param channel room
 * @param event desired event
 * @param cb function, called when event is triggered
 */
fun subscribe(channel: String, event: String, on_connected: () -> Unit, on_event: (String) -> Unit) {

    if (!initialized) init()


    options.query = "apikey=$PUBLIC_API_KEY&contact=${channel}"

    if(::socket.isInitialized){
        Log.e(TAG , "isInitialized")
        socket.disconnect()
        socket.off()
    }

    socket = IO.socket(END_POINT, options)
    socket.on(event) {
        Log.e(TAG, "EVENT_PAYMENT_BACK")
        Log.e(TAG, event + it[0].toString())
        on_event(it[0].toString())
        socket.disconnect()
    }.on(Socket.EVENT_CONNECT) {
        Log.e(TAG, "EVENT_CONNECT")
        on_connected()
    }.on(Socket.EVENT_DISCONNECT) {
        Log.e(TAG, "EVENT_DISCONNECT")
       // socket.connect()
    }.on(Socket.EVENT_RECONNECT) {
        Log.e(TAG, "EVENT_RECONNECT")
    }.on("safety") {
        Log.e(TAG, it[0].toString())
    }
    socket.connect()

}


/**
 * claim new channel for websocket
 * communication
 */
fun claim_channel(cb: (String, String) -> Unit) {

    CLAIM_NEW_CHANNEL_ENDPOINT.httpGet()
            .responseString { req, response, result ->

                when (result) {
                    is Result.Failure -> {
                        Log.e(TAG, String(response.data) + req.method + req.url)
                        val error = String(response.data)
                        when (response.statusCode) {
                            403 -> cb("", STATUS.INVALID_API_KEY.toString())
                            else -> cb("", error)
                        }
                    }

                    is Result.Success -> cb(result.get(), "")

                }
            }
}