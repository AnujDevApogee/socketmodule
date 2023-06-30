package com.apogee.socketlib.listner

import java.io.IOException
import java.io.InputStream

sealed class ConnectionResponse {
    /**
     * OnConnected() return the Response when Socket is connected successfully
     * Use : response attribute to access connection establish response
     */
    class OnConnected(val response: String) : ConnectionResponse()

    /**
     * OnDisconnect() return the Response when Socket is Disconnected successfully
     * Use : reason & code attribute to access it response
     */
    class OnDisconnect(val code: Int, val reason: String) : ConnectionResponse()

    /**
     * OnResponseError() return the exception or cause of failed response
     * Use : exception attribute use to access the error cause
     */
    class OnResponseError(val exception: IOException) : ConnectionResponse()

    /**
     * OnResponse() return the response string
     * Use : response attribute to access the socket response
     */
    class OnResponse(val response: ByteArray) : ConnectionResponse()

    /**
     * OnNetworkConnection() return the response and connection type
     * Use : response & isConnected attribute to access the network state
     */
    class OnNetworkConnection(val response: String, val isConnected: Boolean) : ConnectionResponse()

    /**
     * OnRequestError() return the errorCause string
     * Use : errorCause attribute to access the socket response
     */
    class OnRequestError(val errorCause: String) : ConnectionResponse()

}