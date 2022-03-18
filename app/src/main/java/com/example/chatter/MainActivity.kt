package com.example.chatter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: MutableList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var Auth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = mutableListOf<User>()
        adapter = UserAdapter(this,userList)
        userRecyclerView = findViewById(R.id.rv_Users)
        Auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        fetchUsersFromDb()

        //setting the recycler view with the users fetched from db with the adapter
        userRecyclerView.adapter = adapter
        userRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchUsersFromDb() {
        //fetch data from db - child is the main node in firebase
        //addvalueeventListener is used to receive events about data changes
        dbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    //since we need the data in form of Users we are getting the children value which are in the type of User's class
                    val currentUser = postSnapshot.getValue(User::class.java)
                    //we need to display only the users excluding the logged in user to start chatting hence this check.
                    if(currentUser?.uid != Auth.currentUser?.uid)
                        userList.add(currentUser!!)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                //not required
            }
        }
        )
    }

    //logout option in menu
    //we need to have a option for log out, so created a menu(res/menu/menulogout.xml) and inflated it in the activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menulogout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    //logout functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //check to confirm if the user has chosen the logout option in menu
        if(item.itemId==R.id.item_logout) {
            Auth.signOut()
            val intent = Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}