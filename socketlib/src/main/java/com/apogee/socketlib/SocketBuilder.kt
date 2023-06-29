package com.apogee.socketlib

import com.apogee.socketlib.listner.SocketListener
import com.apogee.socketlib.utils.UtilsFiles
import kotlin.coroutines.CoroutineContext

class SocketBuilder {

    private var ipAddress: String? = null
    private var port: Int? = null
    private var listener: SocketListener? = null
    private var coroutineContext: CoroutineContext? = null

    fun newBuilder(context: CoroutineContext): SocketBuilder {
        this.port = null
        this.ipAddress = null
        this.coroutineContext = context
        return this
    }

    fun addIpAddress(ip: String): SocketBuilder {
        this.ipAddress = ip
        return this
    }

    fun addPort(port: Int): SocketBuilder {
        this.port = port
        return this
    }


    fun addCallBack(listener: SocketListener): SocketBuilder {
        this.listener = listener
        return this
    }

    fun build(): SocketClient {
        if (UtilsFiles.checkValue(port?.toString())) {
            throw IllegalAccessException("Invalid Port")
        }
        if (UtilsFiles.checkValue(ipAddress)) {
            throw IllegalAccessException("Invalid Port IpAddress")
        }

        if (listener == null) {
            throw IllegalAccessException("Cannot find Callback")
        }

        if (coroutineContext==null){
            throw  IllegalAccessException("Context not attacked")
        }

        return SocketClient(
            ipAddress = ipAddress!!,
            port = port!!,
            listener = listener!!,
            coroutineContext = coroutineContext!!
        )
    }

}