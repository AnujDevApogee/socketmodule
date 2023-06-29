package com.apogee.socketlib

import android.content.Context
import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.listner.SocketListener
import com.apogee.socketlib.repo.SocketRepository
import com.apogee.socketlib.utils.UtilsFiles
import com.apogee.socketlib.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SocketClient(
    ipAddress: String,
    port: Int,
    private val listener: SocketListener,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    private var jobConnection: Job? = null
    private var jobWrite: Job? = null

    private val repo = SocketRepository(ipAddress, port)

    private val _state = MutableStateFlow<ConnectionResponse?>(null)
    private val state = _state

    /**
     * Establishes Connection with provided ip Address and Port
     */
    fun establishConnection(context: Context) {
        if (!context.isNetworkAvailable()) {
            _state.value = ConnectionResponse.OnNetworkConnection(
                "No Internet Connection",
                context.isNetworkAvailable()
            )
            return
        }
        jobConnection = launch {
            val response = repo.establishConnection()
            if (response != null) {
                _state.value = response
                repo.listenForIncomingResponse().cancellable().onEach {
                    _state.value = it
                }.collect {
                    UtilsFiles.createLogCat("TESTING_COROUTINE", "It value testing ")
                }
            }
        }
    }

    init {
        listForConnection()
    }

    private fun listForConnection() {
        launch {
            state.onEach { state ->
                state?.let {
                    listener.socketListener(state)
                }
            }.launchIn(this)
        }
    }

    /**
     * Purpose is to disconnect the Socket
     */
    fun disconnect() {
        if (jobConnection != null) {
            _state.value = repo.disconnect(jobConnection!!)
        }
    }

    /**
     * OnRequestSent()
     * Required to a param String type
     * Use to send to data to connected socket
     */

    fun onRequestSent(string: String) {
        if (UtilsFiles.checkValue(string)) {
            _state.value = ConnectionResponse.OnRequestError("Invalid Request Body")
            return
        }
        jobWrite?.cancel()
        jobWrite = launch {
            repo.writeConnection(string)?.let {
                _state.value = it
            }
        }
    }


}