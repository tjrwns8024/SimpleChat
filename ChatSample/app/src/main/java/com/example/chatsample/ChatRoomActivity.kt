package com.example.chatsample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.item_your_chat.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.URISyntaxException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomActivity: AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var message: EditText
    private lateinit var send_btn_message: Button

    private var mSocket: Socket? = null

    var arrayList = arrayListOf<ChatModel>()
    private val mAdapter = ChatAdapter(this, arrayList)

    override fun onStop() {
        super.onStop()
        mSocket?.disconnect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        preferences = getSharedPreferences("usersign", Context.MODE_PRIVATE)

        Log.d("preferences check",preferences.getString("name","nothing ")+"ddfd")

        //어댑터 선언
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)

        send_btn_message = findViewById(R.id.send_btn_message)
        message = findViewById(R.id.message)

            try{
                mSocket = IO.socket("http://localhost:8080/socket.io/")
                mSocket?.connect()
                val result:Boolean = mSocket!!.connected()
                if(result)  Log.d("socket connected", "소켓 연결 성공")
                else Log.e("socket failed", "소캣 실패$result")
            }catch (se: SocketException){
                Log.e("socket", "An exception occurred:\n ${se.printStackTrace()}")
            }
            mSocket?.on("MESSAGE", onNewUser)


            val userId =  preferences.getString("name", "nothing just").toString()
            try {
//                Log.d("username check",preferences.getString("name", "jj"))
//                userId.put("username", preferences.getString("name", "nothing just") + "Connected")
//                Log.e("username", preferences.getString("name", "hjj") + "Connected")
                mSocket?.emit("NAME", userId)
                mSocket?.emit("SEND", message.text.toString())

                Log.d("username check", userId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        send_btn_message.setOnClickListener {
            sendMessage()
        }
    }
    private var onNewUser: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val name: String
            val message: String
            try {
                Log.e("asdasd", data.toString())
                name = data.getString("name")
                message = data.getString("message")


                val format = ChatModel(name, message)
                mAdapter.addItem(format)
                mAdapter.notifyDataSetChanged()
                Log.e("new me",name )
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }
//    internal var onNewMessage:Emitter.Listener = Emitter.Listener { args->
//        runOnUiThread(Runnable {
//            val length = args.size
//
//            if(length == 0){
//                return@Runnable
//            }
//            var username = args[0].toString()
//            try{
//                val b = JSONObject(username)
//                username = b.getString("username")
//            }catch (e: JSONException){
//                e.printStackTrace()
//            }
//        })
//    }

    private fun sendMessage(){

        val script = message.text.toString().trim ({it <= ' '})
        if(TextUtils.isEmpty(script)){
            return
        }
        val item = ChatModel(preferences.getString("name","").toString(),message.text.toString())
        mAdapter.addItem(item)
        mAdapter.notifyDataSetChanged()
        message.setText("")
//        val jsonObject = JSONObject()
//        try{
//            jsonObject.put("name",preferences.getString("name",""))
//            jsonObject.put("message",script)
//        }catch (e:JSONException){
//            e.printStackTrace()
//        }
    }
}