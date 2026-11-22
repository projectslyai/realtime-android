package com.courseexe.realtime

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

typealias EventListener = (data: Any?) -> Unit

class RealtimeChannel(val name: String, private val client: CourseexeRealtimeClient) {
    private val listeners = mutableMapOf<String, MutableList<EventListener>>()

    fun on(event: String, listener: EventListener): RealtimeChannel {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
        return this
    }

    fun emit(event: String, data: Any?) {
        listeners[event]?.forEach { it(data) }
    }
}

class CourseexeRealtimeClient(
    val key: String,
    val host: String = "courseexe.com",
    val port: Int = 443,
    val secure: Boolean = true
) {
    private val gson = Gson()
    private val channels = mutableMapOf<String, RealtimeChannel>()
    private var ws: WebSocketClient? = null

    init {
        connect()
    }

    fun connect() {
        val scheme = if (secure) "wss" else "ws"
        val portString = if ((secure && port == 443) || (!secure && port == 80)) "" else ":$port"
        val uri = URI("$scheme://$host$portString/app/$key?client=courseexe-android&version=1.0.0")

        ws = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                channels.keys.forEach { sendSubscription(it) }
            }

            override fun onMessage(message: String?) {
                message?.let { handleMessage(it) }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {}

            override fun onError(ex: Exception?) {}
        }
        ws?.connect()
    }

    fun subscribe(channelName: String): RealtimeChannel {
        val channel = channels.getOrPut(channelName) { RealtimeChannel(channelName, this) }
        sendSubscription(channelName)
        return channel
    }

    private fun sendSubscription(channelName: String) {
        val payload = mapOf(
            "event" to "pusher:subscribe",
            "data" to mapOf("channel" to channelName)
        )
        ws?.send(gson.toJson(payload))
    }

    private fun handleMessage(raw: String) {
        try {
            val json = gson.fromJson(raw, JsonObject::class.java)
            val event = json.get("event")?.asString ?: return
            val channel = json.get("channel")?.asString

            if (channel != null && channels.containsKey(channel)) {
                channels[channel]?.emit(event, json.get("data"))
            }
        } catch (_: Exception) {}
    }
}
