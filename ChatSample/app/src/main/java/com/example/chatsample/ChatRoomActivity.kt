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
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.json.JSONObject

class ChatRoomActivity: AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    var arrayList = arrayListOf<ChatModel>()
    private val mAdapter = ChatAdapter(this, arrayList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        preferences = getSharedPreferences("usersign", Context.MODE_PRIVATE)

        SocketOb.connectSocket()

        SocketOb.addOnNewUserEvent(onNewUser)

        Log.d("preferences check",preferences.getString("name","nothing "))


        val userId =  preferences.getString("name", "nothing just").toString()
        SocketOb.emitUserId(userId)

        val messageText = message.text.toString()
        if(messageText != ""){
            SocketOb.emitMessageText(messageText)
        }

        Log.d("username check", userId)

        //어댑터 선언
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)

        //http://ec2-18-218-105-177.us-east-2.compute.amazonaws.com:8080/socket.io/

        send_btn_message.setOnClickListener {
            sendMessage()
        }
    }

    override fun onStop() {
        super.onStop()
        SocketOb.disconnectSocket()
    }

    private val onNewUser: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val name: String
            val message: String
            try {
                Log.e("asdasd", data.toString())
                name = data.getString("name")
                message = data.getString("message")
                otherName.text = data.getString("name")

                val format = ChatModel(name, message)
                mAdapter.addItem(format)
                mAdapter.notifyDataSetChanged()
                Log.d("new me",name )
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }
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