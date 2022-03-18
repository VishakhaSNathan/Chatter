package com.example.chatter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    lateinit var chatRecyclerView: RecyclerView
    lateinit var messageBox : EditText
    lateinit var sendButton: ImageView
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: MutableList<Message>
    lateinit var dbRef: DatabaseReference
    var senderUid = FirebaseAuth.getInstance().uid
    var senderRoom:String? = null
    var receiverRoom:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //initialise the values
        chatRecyclerView = findViewById(R.id.rv_Chat)
        messageBox = findViewById(R.id.etMessageBox)
        sendButton = findViewById(R.id.ivsendButton)
        messageList = mutableListOf<Message>()
        messageAdapter= MessageAdapter(this,messageList)
        dbRef = FirebaseDatabase.getInstance().getReference()

        //get the name and uid of the user selected for chatting(i.e the receiver name and uid) from the intent passed from the User Adapter
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //set the action bar title with the receiver name(user selected for chatting)
        supportActionBar?.title = name

        //An unique identifier with the sender and the receiver uid, to save the sender message in a node
        senderRoom = senderUid + receiverUid
        //An unique identifier with the receiver and the sender uid, to save the receiver message in a node
        receiverRoom = receiverUid + senderUid

        getMessageFromDBAndFillTheAdapter()
        chatRecyclerView.adapter = messageAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            saveMessageToDBAndPassItToTheReceiver()
        }

    }

    private fun getMessageFromDBAndFillTheAdapter() {
        //navigates through the nodes chats->senderroom->messages->
        dbRef.child("chats").child(senderRoom!!).child("messages")
                //receive events about data changes
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    //iterate each children message from the data snapshot
                    for(postsnapshot in snapshot.children){
                        // get the message which is of type Message and add to the list
                        val message = postsnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun saveMessageToDBAndPassItToTheReceiver() {
        //fetch message from message box
        val message : String = messageBox.text.toString()
        // create a message with the text and the senderid
        val messageObject = Message(message,senderUid!!)

        if(message.isNotEmpty()) {
            //create a node chats->senderroom->messages-> message, add the message
            dbRef.child("chats").child(senderRoom!!).child("messages")
                .push()
                .setValue(messageObject)
                    //if data stored successfully, then do the below
                .addOnSuccessListener {
                    //create a node chats->receiverroom->messages-> message, add the message
                    dbRef.child("chats").child(receiverRoom!!).child("messages")
                        .push()
                        .setValue(messageObject)
                }
        }
        //clearing the message box post message is sent
        messageBox.setText("")
    }
}