package com.apogee.socketlib

import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.listner.SocketListener
import com.apogee.socketlib.repo.SocketRepository
import com.apogee.socketlib.utils.UtilsFiles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class SocketClient(
    private val ipAddress: String,
    private val port: String,
    private val listener: SocketListener,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    private var jobConnection: Job? = null

    private val repo = SocketRepository()

    private val _state = MutableStateFlow<String>("")
    val state = _state
    fun establishesConnection() {
        jobConnection = launch {
            repo.createConnection().cancellable().onEach {
                _state.value = it
            }.collect {
                UtilsFiles.createLogCat("TESTING_COROUTINE", "It value $it")
            }
        }
    }
    init {
        listForConnection()
    }

    private fun listForConnection() {
        launch {
            state.onEach { state ->
                listener.socketListener(ConnectionResponse.OnConnected(state))
            }.launchIn(this)
        }
    }


    fun disconnect() {
        repo.disconnect(jobConnection!!)
    }


}