package com.example.chatter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user.view.*

class UserAdapter(val context:Context ,private val users: MutableList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_user,parent,false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = users[position]
        //filling the view with text
        holder.itemView.apply {
            tvUsers.text = currentUser.name
        }

        //on clicking the users name, it should navigate to the chat window
        holder.itemView.setOnClickListener {
            val intent = Intent(this.context,ChatActivity::class.java)
            //passing the name of the user selected to the chat activity, so that the user name is displayed in the support action bar
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            //since it is an adapter we are calling the start activity using the context passed
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}