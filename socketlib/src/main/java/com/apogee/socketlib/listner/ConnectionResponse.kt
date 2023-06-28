package com.apogee.socketlib.listner

sealed class ConnectionResponse {

    class OnConnected(val response: String) : ConnectionResponse()

    class OnDisconnect(val code: Int, val reason: String) : ConnectionResponse()

    class OnResponseError(val throwable: Throwable) : ConnectionResponse()

    class OnResponse(val response: String) : ConnectionResponse()

    class OnNetworkConnection(val response: String, val isConnected: Boolean) : ConnectionResponse()

    class OnRequestError(val errorCause: String) : ConnectionResponse()

}