package com.example.phonebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Dashboard : AppCompatActivity(), OnContactClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactList: MutableList<Contact>
    private lateinit var adapter: ContactAdapter
    private lateinit var dbRef: FirebaseFirestore
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactList = mutableListOf()
        adapter = ContactAdapter(contactList, this)
        recyclerView.adapter = adapter

        dbRef = FirebaseFirestore.getInstance()


        snapshotListener = dbRef.collection("contacts")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("Firestore Error", e.message.toString())
                    return@addSnapshotListener
                }


                contactList.clear()


                snapshots?.forEach { document ->
                    val contact = Contact(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        phone = document.getString("phone") ?: "",
                        email = document.getString("email") ?: ""
                    )
                    contactList.add(contact)
                }


                adapter.notifyDataSetChanged()
            }


        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, AddContact::class.java))
        }


        findViewById<ImageButton>(R.id.imageButton6).setOnClickListener {
            logoutUser()
        }
    }

    override fun onContactClick(contact: Contact) {
        val intent = Intent(this, EditContactActivity::class.java).apply {
            putExtra("CONTACT_ID", contact.id)
            putExtra("CONTACT_NAME", contact.name)
            putExtra("CONTACT_PHONE", contact.phone)
            putExtra("CONTACT_EMAIL", contact.email)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        snapshotListener?.remove()
    }

    // Fungsi logout
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()  // Logout dari Firebase
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
