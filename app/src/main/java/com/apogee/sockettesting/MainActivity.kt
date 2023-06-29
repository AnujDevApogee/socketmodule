package com.apogee.sockettesting

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apogee.socketlib.SocketBuilder
import com.apogee.socketlib.SocketClient
import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.listner.SocketListener
import com.apogee.sockettesting.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var socketClient: SocketClient

    companion object {
        const val SERVER_IP = "120.138.10.146"
        const val SERVER_PORT = 8125
    }

    private var sampleUrl = "\r\n\r\nGET /NOIDA2_A HTTP/1.0\n" +
            "User-Agent: NTRIP ApogeeGnss\n" +
            "Authorization: Basic Um92ZXJOb2lkYTpxd2VydHk=\r\n\r\n\r\n\r\n"

    private val callback = object : SocketListener {

        override fun socketListener(conn: ConnectionResponse) {
            when (conn) {
                is ConnectionResponse.OnConnected -> {

                }

                is ConnectionResponse.OnDisconnect -> {

                }

                is ConnectionResponse.OnNetworkConnection -> {

                }

                is ConnectionResponse.OnRequestError -> {

                }

                is ConnectionResponse.OnResponse -> {

                }

                is ConnectionResponse.OnResponseError -> {

                }
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvMessages.movementMethod = ScrollingMovementMethod()

        socketClient = SocketBuilder()
            .newBuilder(lifecycleScope.coroutineContext)
            .addCallBack(listener = callback)
            .addIpAddress(SERVER_IP)
            .addPort(SERVER_PORT)
            .build()


        binding.btnConnect.setOnClickListener {
            socketClient.establishConnection(this)
        }

        binding.btnDissonnect.setOnClickListener {
            socketClient.disconnect()
        }

        binding.btnSend.setOnClickListener {
            socketClient.onRequestSent(sampleUrl)
        }
    }

}