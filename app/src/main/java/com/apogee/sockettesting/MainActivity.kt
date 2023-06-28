package com.apogee.sockettesting

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apogee.socketlib.SocketBuilder
import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.listner.SocketListener
import com.apogee.sockettesting.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val SERVER_IP = "120.138.10.146"
        const val SERVER_PORT = 8125
        private var output: PrintWriter? = null
        private var input: BufferedReader? = null
        var socket: Socket? = null
    }

    private var sampleUrl = "\r\n\r\nGET /NOIDA2_A HTTP/1.0\n" +
            "User-Agent: NTRIP ApogeeGnss\n" +
            "Authorization: Basic Um92ZXJOb2lkYTpxd2VydHk=\r\n\r\n\r\n\r\n"

    private val callback = object : SocketListener {

        override fun socketListener(conn: ConnectionResponse) {
            when (conn) {
                is ConnectionResponse.OnConnected -> {
                    binding.tvMessages.append(conn.response)
                    binding.tvMessages.append("\n")
                }

                is ConnectionResponse.OnDisconnect -> {
                    binding.tvMessages.append(conn.reason)
                    binding.tvMessages.append("\n")
                }

                is ConnectionResponse.OnNetworkConnection -> {
                    binding.tvMessages.append(conn.response)
                    binding.tvMessages.append("\n")
                }

                is ConnectionResponse.OnRequestError -> {
                    binding.tvMessages.append(conn.errorCause)
                    binding.tvMessages.append("\n")
                }

                is ConnectionResponse.OnResponse -> {
                    binding.tvMessages.append(conn.response)
                    binding.tvMessages.append("\n")
                }

                is ConnectionResponse.OnResponseError -> {
                    binding.tvMessages.append("${conn.exception.message}")
                    binding.tvMessages.append("\n")
                }
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvMessages.movementMethod = ScrollingMovementMethod()

        val client = SocketBuilder()
            .newBuilder()
            .addCallBack(listener = callback)
            .addIpAddress(SERVER_IP)
            .addPort(SERVER_PORT)
            .build(lifecycleScope.coroutineContext)


        binding.btnConnect.setOnClickListener {
            client.establishesConnection(this)
//            binding.tvMessages.text = ""
//            val tread = Thread(Thread1())
//            tread.start()
        }

        binding.btnDissonnect.setOnClickListener {
            client.disconnect()
        }

        binding.btnSend.setOnClickListener {
            client.onRequestSent(sampleUrl)
//            val message: String = sampleUrl
//            if (message.isNotEmpty()) {
//                Thread(Thread3(message)).start()
//            }
        }
    }


    internal class Thread1 : Runnable {
        override fun run() {
            try {
                socket = Socket(SERVER_IP, SERVER_PORT)
                output = PrintWriter(socket?.getOutputStream()!!)
                input = BufferedReader(InputStreamReader(socket?.getInputStream()))
                Log.i("Thread_Conn", "run:Status Connected!!")
                Thread(Thread2()).start()
            } catch (e: IOException) {
                Log.i("Thread_Conn", "run:Connection ${e.message}")
                e.printStackTrace()
            }
        }
    }

    internal class Thread2 : Runnable {
        override fun run() {
            while (true) {
                try {
                    val message = input!!.readLine()
                    if (message != null) {
                        Log.i("Thread_Conn", "run: from Server $message")
                    } else {
                        val thread1 = Thread(Thread1())
                        thread1.start()
                        return
                    }
                } catch (e: IOException) {
                    Log.i("Thread_Conn", "run: Input Exception ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }


    inner class Thread3(private val message: String) : Runnable {
        override fun run() {
            output?.write(message)
            output?.flush()
            Log.i("Thread_Conn", "run: Send $message")
        }
    }


}