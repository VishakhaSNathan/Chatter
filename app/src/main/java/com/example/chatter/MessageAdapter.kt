package com.example.chatter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(val context: Context, private val Messages: MutableList<Message>):
// implementing plain recycler view holder, since we have two view holders
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val ITEM_RECEIVED = 1
    private val ITEM_SENT = 2

    //since we have two types of message (message sent and received),
    // we should know if the layout to be chosen is sender or receiver.
    // For that we are overriding the inbuilt method, to get the corresponding view type
    override fun getItemViewType(position: Int): Int {
        val currentMessage = Messages[position]
        // All messages have message content and sender id.
        // if the sender id is the currentuser.id then it is the sent message else recieved message
        if(currentMessage.senderId.equals(FirebaseAuth.getInstance().currentUser?.uid)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVED
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.tv_sentMessage)
    }
    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById(R.id.tv_receivedMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==ITEM_RECEIVED){
            return ReceivedViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.activity_receivedmessage,parent,false)
            )
        }
        else
        {
            return SentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.activity_sentmessage,parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = Messages[position]
        // based on the holder, setting the value of the recyler view
        if(holder.javaClass==SentViewHolder::class.java){
            val holder = holder as SentViewHolder
            holder.sentMessage.text =  currentMessage.message
        }
        else{
            val holder = holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return Messages.size
    }
}