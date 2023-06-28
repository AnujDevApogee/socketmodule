package com.apogee.socketlib.listner

interface SocketListener {
    fun webSocketListener(conn: ConnectionResponse)
}