package com.apogee.socketlib.listner

interface SocketListener {
    fun socketListener(conn: ConnectionResponse)
}