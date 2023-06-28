package com.apogee.socketlib.repo

import com.apogee.socketlib.utils.UtilsFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.PrintWriter
import java.net.Socket


class SocketRepository {

    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
    var socket: Socket? = null

    fun createConnection() = flow {
        while (true) {
            doConnection()
            emit(true)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun doConnection()= withContext(Dispatchers.IO) {
        while (isActive){
            UtilsFiles.createLogCat("TESTING_CONNECTION", "TestingConnection")
        }
    }


    fun disconnect(job:Job){
        job.cancel("testing completed")
    }




}