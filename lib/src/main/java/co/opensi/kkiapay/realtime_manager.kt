package co.opensi.kkiapay

import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
/**
 * Created by geecko on 5/23/18.
 */


/**
 * Some treatment in the KKIAPAY API are delayed.
 * While a streaming layer above the REST is needed
 * to listen notifications is needed. More details at https://docs.kkiapay.com/v1/websocket.
 * realtime_manager exposes the functions for subscribe to the different API notifications
 */

lateinit var options: PusherOptions
lateinit var pusher : Pusher
var connected = false

fun init(){

    options =  PusherOptions()
    options.setCluster("eu")
    pusher = Pusher(PUBLIC_API_KEY, options)
    pusher.connect()
    connected = true

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
fun subscribe(channel:String,event: String, cb: (String)-> Unit) {

    if( !connected ) init()

    val channel_manager = pusher.subscribe(channel)

    channel_manager.bind(event, {
        _,_,data ->

        cb(data)
    })

}
