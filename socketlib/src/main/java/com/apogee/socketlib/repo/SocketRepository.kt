package com.apogee.socketlib.repo

import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.utils.UtilsFiles
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
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.CancellationException


class SocketRepository(
    private val ip: String, private val port: Int
) {

    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
    private var socket: Socket? = null

    fun listenForIncomingResponse() = flow {
        while (currentCoroutineContext().isActive) {
            try {
                val message = input?.readLine()
                UtilsFiles.createLogCat("testing_conn","testing... $message")
                if (message != null) {
                    emit(ConnectionResponse.OnResponse(message))
                }
            } catch (e: IOException) {
                UtilsFiles.createLogCat("testing_conn", "Reading Eeception${e.message}")
                emit(ConnectionResponse.OnResponseError(e))
                currentCoroutineContext().cancel(CancellationException())
            }
            delay(100)
        }
    }.flowOn(Dispatchers.IO)


    suspend fun writeConnection(requestBody: String) = withContext(Dispatchers.IO) {
        if (isActive) {
            try {
                output?.write(requestBody)
                output?.flush()
                null
            } catch (e: Exception) {
                UtilsFiles.createLogCat("testing_conn", "Write error ${e.message}")
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
                input = BufferedReader(InputStreamReader(socket?.getInputStream()))
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
        return ConnectionResponse.OnDisconnect(400,"Disconnected Manually")
    }


}