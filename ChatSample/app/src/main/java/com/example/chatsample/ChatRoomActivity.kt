package com.example.chatsample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat_room.*
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomActivity: AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var message: EditText
    private lateinit var send_btn_message: Button

    var arrayList = arrayListOf<ChatModel>()
    private val mAdapter = ChatAdapter(this, arrayList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)

        //어댑터 선언
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)

        send_btn_message = findViewById(R.id.send_btn_message)
        message = findViewById(R.id.message)

        send_btn_message.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage(){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("HH:mm:ss",Locale.KOREA)

        val getTime = dateFormat.format(date)

        val item = ChatModel(preferences.getString("name","").toString(),message.text.toString(),getTime)
        mAdapter.addItem(item)
        mAdapter.notifyDataSetChanged()

        message.setText("")
    }
}