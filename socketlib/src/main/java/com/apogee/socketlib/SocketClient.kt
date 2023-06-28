package com.apogee.socketlib

import com.apogee.socketlib.listner.SocketListener
import com.apogee.socketlib.repo.SocketRepository
import com.apogee.socketlib.utils.UtilsFiles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.CoroutineContext

class SocketClient(
    private val ipAddress: String,
    private val port: String,
    private val listener: SocketListener,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    private var jobConnection: Job? = null

    private val repo = SocketRepository()

    fun establishesConnection() {
        jobConnection = async(Dispatchers.IO) {
            repo.createConnection().cancellable().collectLatest {
                UtilsFiles.createLogCat("TESTING_COROUTINE","It value $it")
            }
        }
    }


    fun disconnect() {
        repo.disconnect(jobConnection!!)
    }


}