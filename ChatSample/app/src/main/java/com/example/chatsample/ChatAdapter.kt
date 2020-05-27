package com.example.chatsample

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val context: Context, private val arrayList: ArrayList<ChatModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var preferences: SharedPreferences

    fun addItem(item: ChatModel) {//아이템 추가
        arrayList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        //getItemViewType 에서 뷰타입 1을 리턴받았다면 내채팅레이아웃을 받은 Holder를 리턴
        return if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_chat, parent, false)
            Holder(view)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_your_chat, parent, false)
            Holder2(view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (viewHolder is Holder) {
            (viewHolder).chatMessage?.text = arrayList[i].message
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if (viewHolder is Holder2) {
            (viewHolder).chatFriendName?.text = arrayList[i].name
            (viewHolder).chatMessage?.text = arrayList[i].message
        }

    }

    //내가친 채팅 뷰홀더
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록<TextView> 모델의 변수들 정의하는부분
        val chatMessage = itemView.findViewById<TextView>(R.id.chat_Text)
    }

    //상대가친 채팅 뷰홀더
    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //친구목록 모델의 변수들 정의하는부분
        val chatFriendName = itemView.findViewById<TextView>(R.id.chat_You_Name)
        val chatMessage = itemView.findViewById<TextView>(R.id.chat_Text)


    }

    override fun getItemViewType(position: Int): Int {
        preferences = context.getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (arrayList[position].name == preferences.getString("name", "")) {
            2
        } else {
            1
        }
    }
}