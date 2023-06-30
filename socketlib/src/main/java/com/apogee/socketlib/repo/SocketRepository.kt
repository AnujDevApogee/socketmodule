package com.apogee.socketlib.repo

import com.apogee.socketlib.listner.ConnectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.Arrays
import java.util.concurrent.CancellationException


class SocketRepository(
    private val ip: String, private val port: Int
) {

    private var output: PrintWriter? = null
    private var input: DataInputStream? = null
    private var socket: Socket? = null

    fun listenForIncomingResponse() = flow {
        while (currentCoroutineContext().isActive) {
            try {
                val byte_data = ByteArray(5000)
                var read = 0
                if (input != null) {
                    if (input!!.available() > 0) {
                        read = input!!.read(byte_data)
                        emit(ConnectionResponse.OnResponse(byte_data.copyOf(read)))
                    }
                }
            } catch (e: IOException) {
                emit(ConnectionResponse.OnResponseError(e))
                currentCoroutineContext().cancel(CancellationException())
            }
        }
    }.flowOn(Dispatchers.IO)


    suspend fun writeConnection(requestBody: String) = withContext(Dispatchers.IO) {
        if (isActive) {
            try {
                output?.write(requestBody)
                output?.flush()
                null
            } catch (e: Exception) {
                // UtilsFiles.createLogCat("testing_conn", "Write error ${e.message}")
                ConnectionResponse.OnRequestError("${e.message}")
            }
        } else {
            null
        }
    }


    suspend fun establishConnection() = withContext(Dispatchers.IO) {
        return@withContext if (isActive) {
            try {
                socket = Socket(ip, port)
                output = PrintWriter(socket?.getOutputStream()!!)
                input = DataInputStream(socket?.getInputStream())
                ConnectionResponse.OnConnected("Connected")
            } catch (e: IOException) {
                ConnectionResponse.OnResponseError(e)
            }
        } else {
            null
        }
    }


    fun disconnect(job: Job): ConnectionResponse.OnDisconnect {
        socket?.close()
        output?.close()
        input?.close()
        job.cancel("testing completed")
        return ConnectionResponse.OnDisconnect(400, "Disconnected Manually")
    }


}