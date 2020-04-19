package com.example.chatsample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        send_btn_userName.setOnClickListener {
            editor.putString("name", userName.text.toString())
            val intent = Intent(this, ChatRoomActivity::class.java)
            startActivity(intent)
        }
    }
}
