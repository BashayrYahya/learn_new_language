package com.example.learn_new_language.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.learn_new_language.R
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(val context: Context , private val messageList: ArrayList<MessageData>): RecyclerView.Adapter< RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED =1
    val ITEM_SENt = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == 1){   // inflate receive
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.msg_received, parent, false)
            ReceiveViewHolder(view)
        } else {      // inflate send
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.send_msg, parent, false)
            SendViewHolder(view)

        }






}



    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){

            ITEM_SENt
        }else{
            ITEM_RECEIVED
        }


    }


    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: $messageList")
        return messageList.size}

    inner class SendViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage : TextView = itemView.findViewById(R.id.msg_send)


    }

    inner class ReceiveViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage : TextView = itemView.findViewById(R.id.message_received)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        Log.d("TsAG", "onBindViewHolder: ${messageList.size}")
        if (holder.javaClass == SendViewHolder::class.java){

            // do stuff for send view holder
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage.text = currentMessage.message


        }else{
            // do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
        }


    }


}