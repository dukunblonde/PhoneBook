package com.example.phonebook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddContact : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)

        firestore = FirebaseFirestore.getInstance()

        val nameInput = findViewById<EditText>(R.id.editTextText5)
        val phoneInput = findViewById<EditText>(R.id.editTextText6)
        val emailInput = findViewById<EditText>(R.id.editTextText2)
        val addButton = findViewById<Button>(R.id.button2)

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val phone = phoneInput.text.toString()
            val email = emailInput.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()) {
                val contact = hashMapOf(
                    "name" to name,
                    "phone" to phone,
                    "email" to email
                )

                firestore.collection("contacts")
                    .add(contact)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()
                        finish() 
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
