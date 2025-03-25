package com.example.phonebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditContactActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var dbRef: FirebaseFirestore

    private var contactId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        etName = findViewById(R.id.editName)
        etPhone = findViewById(R.id.editPhone)
        etEmail = findViewById(R.id.editEmail)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        dbRef = FirebaseFirestore.getInstance()

        contactId = intent.getStringExtra("CONTACT_ID")!!
        etName.setText(intent.getStringExtra("CONTACT_NAME"))
        etPhone.setText(intent.getStringExtra("CONTACT_PHONE"))
        etEmail.setText(intent.getStringExtra("CONTACT_EMAIL"))

        btnUpdate.setOnClickListener {
            val updatedName = etName.text.toString()
            val updatedPhone = etPhone.text.toString()
            val updatedEmail = etEmail.text.toString()

            if (updatedName.isNotEmpty() && updatedPhone.isNotEmpty() && updatedEmail.isNotEmpty()) {
                dbRef.collection("contacts").document(contactId)
                    .update("name", updatedName, "phone", updatedPhone, "email", updatedEmail)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kontak berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            }
        }


        btnDelete.setOnClickListener {
            dbRef.collection("contacts").document(contactId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Kontak berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menghapus: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
